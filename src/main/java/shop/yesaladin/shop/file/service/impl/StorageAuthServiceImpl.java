package shop.yesaladin.shop.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.shop.common.exception.CustomJsonProcessingException;
import shop.yesaladin.shop.config.ObjectStorageProperties;
import shop.yesaladin.shop.file.dto.TokenJsonDto;
import shop.yesaladin.shop.file.dto.TokenRequest;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

/**
 * Object Storage 인증 토큰을 발급받기 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class StorageAuthServiceImpl implements StorageAuthService {

    private final ObjectStorageProperties objectStorage;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    /**
     * 요청 본문(tenantId, username, password)을 생성합니다.
     *
     * @return 생성된 요청 본문
     * @author 이수정
     * @since 1.0
     */
    public TokenRequest makeTokenRequest() {
        TokenRequest tokenRequest = new TokenRequest();

        tokenRequest.getAuth().setTenantId(objectStorage.getTenantId());
        tokenRequest.getAuth().getPasswordCredentials().setUsername(objectStorage.getUsername());
        tokenRequest.getAuth().getPasswordCredentials().setPassword(objectStorage.getPassword());

        return tokenRequest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthToken() {
        // 헤더 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(makeTokenRequest(), httpHeaders);

        // 토큰 요청
        ResponseEntity<String> response = restTemplate.exchange(
                objectStorage.getAuthUrl(),
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        // Json 매핑
        try {
            TokenJsonDto tokenJsonDto = mapper.readValue(response.getBody(), TokenJsonDto.class);

            return tokenJsonDto.getAccess().getToken().getId();
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException(e);
        }


    }

}

