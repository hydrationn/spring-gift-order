package gift.order.event.listener;

import gift.auth.service.KakaoMessageService;
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
        String token = String.valueOf(memberRepository.findById(event.memberId()).get().getKakaoToken());
        kakaoMessageService.sendOrderMemo(token, event.order());
    }
}
