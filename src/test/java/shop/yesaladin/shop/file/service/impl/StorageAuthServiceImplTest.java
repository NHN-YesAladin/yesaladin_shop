package shop.yesaladin.shop.file.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

class StorageAuthServiceImplTest {

    @Value("${storage-token.auth-url}")
    private String authUrl;
    @Value("${storage-token.tenant-id}")
    private String tenantId;
    @Value("${storage-token.username}")
    private String username;
    @Value("${storage-token.password}")
    private String password;

    private StorageAuthService service;

    @BeforeEach
    void setUp() {
        service = new StorageAuthServiceImpl();
    }

    @Test
    @DisplayName("오브젝트 스토리지 토큰 발급")
    void getAuthToken() {
        // TODO: 테스트 미완성

        // when
        // String token = service.getAuthToken();

        // then
        // assertThat(token).isNotNull();
    }
}