package gift.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.dto.AuthTokenResponseDto;
import gift.auth.service.AuthService;
import gift.member.service.MemberService;
import gift.security.config.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoAuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public KakaoAuthController(AuthService authService, MemberService memberService, JwtProvider jwtProvider) {
        this.authService = authService;
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping(value = "/", params = "code")
    public ResponseEntity<AuthTokenResponseDto> kakaoLogin(
            @RequestParam("code") String authorizationCode
    ) throws JsonProcessingException {
        AuthTokenResponseDto response = authService.loginWithKakao(authorizationCode);
        return ResponseEntity.ok(response);
    }
}
