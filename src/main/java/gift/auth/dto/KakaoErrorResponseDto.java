package gift.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoErrorResponseDto {
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}
