package gift.auth.dto;

public record KakaoFeedRequestDto(
        String object_type,
        KakaoContent content
) {}