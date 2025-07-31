package gift.auth.repository;

import gift.auth.entity.KakaoToken;
import gift.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
    Optional<KakaoToken> findByMember(Member member);
}
