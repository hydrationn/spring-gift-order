package gift.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.dto.AuthTokenResponseDto;
import gift.auth.dto.KakaoTokenResponse;
import gift.member.entity.Member;
import gift.member.service.MemberService;
import gift.security.config.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    public AuthTokenResponseDto loginWithKakao(String code) throws JsonProcessingException {
        KakaoTokenResponse kakaoToken = kakaoOAuthService.requestAccessToken(code);

        String profileJson = kakaoOAuthService.requestRawUserInfo(kakaoToken.getAccessToken());

        JsonNode root = objectMapper.readTree(profileJson);
        String email = root.path("kakao_account").path("email").asText();
        String nickname = root.path("properties").path("nickname").asText();

        Member member = memberService.registerOrUpdate(email, nickname, kakaoToken.getAccessToken());

        String jwt = jwtProvider.generateToken(member);
        return new AuthTokenResponseDto(jwt);
    }
}
