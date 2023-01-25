package shop.yesaladin.shop.file.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.config.ObjectStorageProperties;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

import static org.mockito.Mockito.mock;


@RequiredArgsConstructor
class StorageAuthServiceImplTest {

    private StorageAuthService service;
    private ObjectStorageProperties objectStorage;

    @BeforeEach
    void setUp() {
        objectStorage = mock(ObjectStorageProperties.class);
        service = new StorageAuthServiceImpl(
                objectStorage
        );
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