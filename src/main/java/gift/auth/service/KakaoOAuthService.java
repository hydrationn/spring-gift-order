package gift.auth.service;

import gift.auth.dto.KakaoTokenResponse;
import gift.auth.exception.KakaoAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-url}")
    private String tokenUrl;

    private final RestTemplate restTemplate;

    public KakaoTokenResponse requestAccessToken(String authorizationCode) {
        URI tokenUri = URI.create(tokenUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
                .post(tokenUri)
                .headers(headers)
                .body(body);

        try {
            ResponseEntity<KakaoTokenResponse> response =
                    restTemplate.exchange(request, KakaoTokenResponse.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new KakaoAuthException("토큰 응답이 비정상입니다: " + response.getStatusCode());
            }
            return response.getBody();

        } catch (HttpClientErrorException e) {
            throw new KakaoAuthException(
                    "카카오 토큰 요청 실패: " + e.getStatusCode() + " / " + e.getResponseBodyAsString(), e
            );
        }
    }
}
