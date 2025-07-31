package gift.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.dto.AuthTokenResponseDto;
import gift.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoAuthController {

    private final AuthService authService;

    public KakaoAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(value = "/", params = "code")
    public ResponseEntity<AuthTokenResponseDto> kakaoLogin(
            @RequestParam("code") String authorizationCode
    ) throws JsonProcessingException {
        AuthTokenResponseDto response = authService.loginWithKakao(authorizationCode);
        return ResponseEntity.ok(response);
    }
}
