package gift.auth.service;

import gift.auth.entity.KakaoToken;
import gift.auth.repository.KakaoTokenRepository;
import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class KakaoTokenService {
    private final MemberRepository memberRepository;
    private final KakaoTokenRepository kakaoTokenRepository;

    @Transactional
    public void saveOrUpdateToken(Long memberId,
                                  String accessToken,
                                  String refreshToken,
                                  Instant expiresIn) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        kakaoTokenRepository.findByMember(member)
                .map(token -> {
                    token.updateTokens(accessToken, refreshToken, expiresIn);
                    return token;
                })
                .orElseGet(() -> {
                    KakaoToken token = new KakaoToken(accessToken, refreshToken, expiresIn);
                    token.linkMember(member);
                    return kakaoTokenRepository.save(token);
                });
    }
}
