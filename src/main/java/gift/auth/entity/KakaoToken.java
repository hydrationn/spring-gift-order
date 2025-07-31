package gift.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.member.entity.Member;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "kakao_token")
public class KakaoToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("access_token")
    @Column(name = "access_token", length = 2048)
    private String accessToken;

    @JsonProperty("refresh_token")
    @Column(name = "refresh_token", length = 2048)
    private String refreshToken;

    @JsonProperty("expires_in")
    @Column(name = "expires_in")
    private Instant expiresIn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false,  unique = true)
    private Member member;

    protected KakaoToken() {
    }

    public KakaoToken(String accessToken, String refreshToken, Instant expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public void updateTokens(String accessToken,
                             String refreshToken,
                             Instant expiresIn) {
        this.accessToken  = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn    = expiresIn;
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getExpiresIn() {
        return expiresIn;
    }

    public Member getMember() {
        return member;
    }

    public void linkMember(Member member) {
        this.member = member;
        if (member.getKakaoToken() != this) {
            member.setKakaoToken(this);
        }
    }

    public void unlinkMember() {
        this.member = null;
    }
}