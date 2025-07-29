package gift.order.service;

import gift.auth.service.KakaoMessageService;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import gift.option.entity.Option;
import gift.option.exception.OptionNotFoundException;
import gift.option.repository.OptionRepository;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final KakaoMessageService kakaoMessageService;

    public OrderServiceImpl(
            MemberRepository memberRepository,
            OptionRepository optionRepository,
            OrderRepository orderRepository,
            WishRepository wishRepository,
            KakaoMessageService kakaoMessageService
    ) {
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long memberId, OrderRequestDto dto) {
        Option opt = optionRepository.findById(dto.optionId())
                .orElseThrow(() -> new OptionNotFoundException(dto.optionId()));

        Order order = new Order(opt, dto.quantity(), LocalDateTime.now(), dto.message());
        orderRepository.save(order);

        wishRepository.deleteByOption(opt);

        String accessToken = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId))
                .getKakaoAccessToken();
        kakaoMessageService.sendOrderMemo(accessToken, order);

        return new OrderResponseDto(
                order.getId(),
                opt.getId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }

    @Override
    public Page<OrderResponseDto> listOrders(Long memberId, Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);
        return page.map(o -> new OrderResponseDto(
                o.getId(),
                o.getOption().getId(),
                o.getQuantity(),
                o.getOrderDateTime(),
                o.getMessage()
        ));
    }
}

