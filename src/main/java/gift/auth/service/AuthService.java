package gift.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.dto.AuthTokenResponseDto;
import gift.auth.entity.KakaoToken;
import gift.auth.dto.KakaoUserResponseDto;
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
    private final KakaoTokenService kakaoTokenService;
    private final JwtProvider jwtProvider;

    public AuthTokenResponseDto loginWithKakao(String code) throws JsonProcessingException {
        KakaoToken kakaoToken = kakaoOAuthService.requestAccessToken(code);

        KakaoUserResponseDto profile = kakaoOAuthService.requestUserInfo(kakaoToken.getAccessToken());

        Member member = memberService.registerOrUpdate(
                profile.email(),
                profile.nickname()
        );

        kakaoTokenService.saveOrUpdateToken(
                member.getId(),
                kakaoToken.getAccessToken(),
                kakaoToken.getRefreshToken(),
                kakaoToken.getExpiresIn()
        );

        String jwt = jwtProvider.generateToken(member);
        return new AuthTokenResponseDto(jwt);
    }
}
