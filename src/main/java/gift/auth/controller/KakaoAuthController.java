package gift.auth.controller;

import gift.auth.dto.KakaoTokenResponse;
import gift.auth.service.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @GetMapping(value = "/", params = "code")
    public ResponseEntity<KakaoTokenResponse> callback(
            @RequestParam("code") String authorizationCode
    ) {
        KakaoTokenResponse token = kakaoOAuthService.requestAccessToken(authorizationCode);
        return ResponseEntity.ok(token);
    }
}
