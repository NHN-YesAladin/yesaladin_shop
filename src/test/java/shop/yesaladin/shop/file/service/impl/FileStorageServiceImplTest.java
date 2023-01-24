package shop.yesaladin.shop.file.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.shop.file.service.inter.FileStorageService;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FileStorageServiceImplTest {

    @Value("${storage-token.storage-url}")
    private String storageUrl;
    @Value("${storage-token.storage-account}")
    private String storageAccount;
    @Value("${storage-token.container-name}")
    private String containerName;

    private FileStorageService service;
    private StorageAuthService storageAuthService;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        storageAuthService = mock(StorageAuthServiceImpl.class);
        service = new FileStorageServiceImpl(
                storageAuthService
        );

        restTemplate = new RestTemplate();
    }

    @Test
    @DisplayName("파일 업로드 성공")
    void fileUpload() throws IOException {
        // TODO: 테스트 미완성

        // when
        MultipartFile multipartFile = new MockMultipartFile("image", new FileInputStream("src/test/resources/img/yesaladinnotfound.png"));

        // service.fileUpload("domain", "type", multipartFile);
    }
}