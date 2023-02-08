package shop.yesaladin.shop.file.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.shop.config.ObjectStorageProperties;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

import static org.mockito.Mockito.mock;

class StorageAuthServiceImplTest {

    private StorageAuthService service;

    @BeforeEach
    void setUp() {
        ObjectStorageProperties objectStorage = mock(ObjectStorageProperties.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        RedisTemplate<String, String> redisTemplate = mock(RedisTemplate.class);

        service = new StorageAuthServiceImpl(
                objectStorage,
                restTemplate,
                objectMapper,
                redisTemplate
        );
    }

    @Test
    @DisplayName("오브젝트 스토리지 토큰 발급")
    void getAuthToken() {
    }
}