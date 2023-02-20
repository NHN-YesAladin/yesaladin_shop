package shop.yesaladin.shop.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.shop.common.exception.CustomJsonProcessingException;
import shop.yesaladin.shop.config.ObjectStorageProperties;
import shop.yesaladin.shop.file.dto.TokenDto;
import shop.yesaladin.shop.file.dto.TokenJsonDto;
import shop.yesaladin.shop.file.dto.TokenRequest;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Object Storage 인증 토큰을 발급받기 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class StorageAuthServiceImpl implements StorageAuthService {

    private static final String OBJECT_STORAGE_TOKEN_KEY = "object-storage-token";

    private final ObjectStorageProperties objectStorage;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 요청 본문(tenantId, username, password)을 생성합니다.
     *
     * @return 생성된 요청 본문
     * @author 이수정
     * @since 1.0
     */
    private TokenRequest makeTokenRequest() {
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
        // Redis에 토큰 존재하는지 확인 => object-storage-token
        String tokenInRedis = redisTemplate.opsForValue().get(OBJECT_STORAGE_TOKEN_KEY);
        if (Objects.nonNull(tokenInRedis)) {
            log.info("token in redis = {}", tokenInRedis);
            return tokenInRedis;
        }

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
            TokenDto token = tokenJsonDto.getAccess().getToken();

            // 만료일 파싱
            Instant instant = Instant.parse(token.getExpires());
            LocalDateTime expireTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));

            // 만료시간 - 현재시간 = 3시간 가량
            LocalDateTime now = LocalDateTime.now();
            long timeout = Duration.between(now, expireTime).toSeconds();

            redisTemplate.opsForValue().append(OBJECT_STORAGE_TOKEN_KEY, token.getId());
            redisTemplate.expire(OBJECT_STORAGE_TOKEN_KEY, timeout, TimeUnit.SECONDS);

            log.info("new token = {}", token.getId());
            return token.getId();
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException(e);
        }
    }

}

