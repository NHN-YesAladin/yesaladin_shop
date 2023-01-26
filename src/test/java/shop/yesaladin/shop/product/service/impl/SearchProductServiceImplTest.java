package shop.yesaladin.shop.product.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.shop.file.dto.TokenRequest;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

@SpringBootTest
class SearchProductServiceImplTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    StorageAuthService storageAuthService;
    @Value("${storage-token.storage-url}")
    private String objectStorageUrl;
    @Value("${storage-token.storage-account}")
    private String account;
    @Value("${storage-token.container-name}")
    private String container;
    @Value("${storage-token.thumbnail-path}")
    private String thumbnailPath;

    @Test
    void test() {
        String token = storageAuthService.getAuthToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("X-Auth-Token", token);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        String url = "https://api-storage.cloud.toast.com/v1/AUTH_fcb81f74e379456b8ca0e091d351a7af/yesaladinTest?path=product/thumbnail";
//
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//
//        System.out.println(Objects.requireNonNull(response.getBody()).contains("fe13fa77-4058-4705-a8f9-05160c4439b0.jpeg"));

        String uri = UriComponentsBuilder.fromHttpUrl(objectStorageUrl.concat("/")).path(account.concat("/").concat(container))
                .queryParam("path", thumbnailPath).toUriString();

        System.out.println(uri);

        ResponseEntity<String> response1 = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        System.out.println(response1.getBody());
    }
}