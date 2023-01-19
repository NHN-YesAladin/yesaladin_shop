package shop.yesaladin.shop.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.shop.common.exception.CustomJsonProcessingException;
import shop.yesaladin.shop.file.dto.TokenJsonDto;
import shop.yesaladin.shop.file.dto.TokenRequest;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

/**
 * Object Storage 인증 토큰을 발급받기 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@Service
public class StorageAuthServiceImpl implements StorageAuthService {

    @Value("${storage-token.auth-url}")
    private String authUrl;
    @Value("${storage-token.tenant-id}")
    private String tenantId;
    @Value("${storage-token.username}")
    private String username;
    @Value("${storage-token.password}")
    private String password;

    /**
     * 요청 본문(tenantId, username, password)을 생성합니다.
     *
     * @return 생성된 요청 본문
     * @author 이수정
     * @since 1.0
     */
    public TokenRequest makeTokenRequest() {
        TokenRequest tokenRequest = new TokenRequest();

        tokenRequest.getAuth().setTenantId(tenantId);
        tokenRequest.getAuth().getPasswordCredentials().setUsername(username);
        tokenRequest.getAuth().getPasswordCredentials().setPassword(password);

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
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                authUrl,
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        // Json 매핑
        try {
            ObjectMapper mapper = new JsonMapper();
            TokenJsonDto tokenJsonDto = mapper.readValue(response.getBody(), TokenJsonDto.class);

            return tokenJsonDto.getAccess().getToken().getId();
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException(e);
        }
    }

}

