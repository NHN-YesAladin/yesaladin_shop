package shop.yesaladin.shop.file.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.shop.config.ObjectStorageProperties;
import shop.yesaladin.shop.file.dto.AccessDto;
import shop.yesaladin.shop.file.dto.TokenDto;
import shop.yesaladin.shop.file.dto.TokenJsonDto;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

class StorageAuthServiceImplTest {

    private static final String OBJECT_STORAGE_TOKEN_KEY = "object-storage-token";

    private ObjectStorageProperties objectStorage;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations valueOperations;

    private StorageAuthService service;

    @BeforeEach
    void setUp() {
        objectStorage = mock(ObjectStorageProperties.class);
        restTemplate = mock(RestTemplate.class);
        objectMapper = mock(ObjectMapper.class);
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);

        service = new StorageAuthServiceImpl(
                objectStorage,
                restTemplate,
                objectMapper,
                redisTemplate
        );
    }

    @Test
    @DisplayName("오브젝트 스토리지 토큰 발급_이미 존재하는 경우")
    void getAuthToken_alreadyExist() {
        // given
        String tokenInRedis = "token";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(OBJECT_STORAGE_TOKEN_KEY)).thenReturn(tokenInRedis);

        // when
        String authToken = service.getAuthToken();

        // then
        assertThat(authToken).isEqualTo(tokenInRedis);

        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).get(OBJECT_STORAGE_TOKEN_KEY);
    }

    @Test
    @DisplayName("오브젝트 스토리지 토큰 발급")
    void getAuthToken() throws JsonProcessingException {
        // given
        when(objectStorage.getAuthUrl()).thenReturn("");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(OBJECT_STORAGE_TOKEN_KEY)).thenReturn(null);

        ResponseEntity response = mock(ResponseEntity.class);
        when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
                .thenReturn(response);
        when(response.getBody()).thenReturn("");

        TokenDto tokenDto = mock(TokenDto.class);
        AccessDto accessDto = new AccessDto(tokenDto);
        TokenJsonDto tokenJsonDto = new TokenJsonDto(accessDto);
        when(objectMapper.readValue(anyString(), ArgumentMatchers.<Class<TokenJsonDto>>any()))
                .thenReturn(tokenJsonDto);
        when(tokenDto.getExpires()).thenReturn("2018-01-15T08:05:05Z");
        when(tokenDto.getId()).thenReturn("token");

        // when
        String authToken = service.getAuthToken();

        // then
        assertThat(authToken).isEqualTo("token");

        verify(redisTemplate, times(2)).opsForValue();
        verify(restTemplate, times(1)).exchange(anyString(), any(), any(), any(Class.class));
    }
}