package gift.order.event.listener;

import gift.auth.service.KakaoMessageService;
import gift.member.entity.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import gift.order.event.OrderCreatedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KakaoOrderListener {
    private final MemberRepository memberRepository;
    private final KakaoMessageService kakaoMessageService;

    public KakaoOrderListener(MemberRepository memberRepository, KakaoMessageService kakaoMessageService) {
        this.memberRepository = memberRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        Member member = memberRepository.findById(event.memberId())
                .orElseThrow(() -> new MemberNotFoundException(event.memberId()));

        String accessToken = member.getKakaoToken().getAccessToken();

        kakaoMessageService.sendOrderMemo(accessToken, event.order());
    }
}
