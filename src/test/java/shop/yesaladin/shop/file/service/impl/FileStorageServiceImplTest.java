package shop.yesaladin.shop.file.service.impl;

import static org.mockito.Mockito.mock;

import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.shop.config.ClockConfiguration;
import shop.yesaladin.shop.config.ObjectStorageProperties;
import shop.yesaladin.shop.file.service.inter.ObjectStorageService;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

class FileStorageServiceImplTest {

    private ObjectStorageService service;
    private StorageAuthService storageAuthService;

    private RestTemplate restTemplate;
    private ObjectStorageProperties objectStorage;
    private ClockConfiguration clock;

    @BeforeEach
    void setUp() {
        storageAuthService = mock(StorageAuthServiceImpl.class);
        restTemplate = mock(RestTemplate.class);
        objectStorage = mock(ObjectStorageProperties.class);
        clock = mock(ClockConfiguration.class);

//        service = new FileStorageServiceImpl(
//                storageAuthService,
//                restTemplate,
//                objectStorage,
//                clock
//        );
    }

    @Disabled
    @Test
    @DisplayName("파일 업로드 성공")
    void fileUpload() throws IOException {
        // TODO: 테스트 미완성

        // when
        MultipartFile multipartFile = new MockMultipartFile(
                "image",
                new FileInputStream("src/test/resources/img/yesaladinnotfound.png")
        );

        // service.fileUpload("domain", "type", multipartFile);
    }
}