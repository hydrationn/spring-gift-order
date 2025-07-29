package gift.order.dto;

public record OrderRequestDto(
        Long optionId,
        int quantity,
        String message
) {
}
