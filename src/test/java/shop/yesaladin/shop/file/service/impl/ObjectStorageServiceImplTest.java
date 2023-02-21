package shop.yesaladin.shop.file.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.shop.config.ObjectStorageProperties;
import shop.yesaladin.shop.file.dto.FileUploadResponseDto;
import shop.yesaladin.shop.file.service.inter.ObjectStorageService;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

class ObjectStorageServiceImplTest {

    private final Clock clock = Clock.fixed(
            Instant.parse("2023-02-01T00:00:00.00Z"),
            ZoneId.of("UTC")
    );
    private ObjectStorageService service;
    private StorageAuthService storageAuthService;
    private RestTemplate restTemplate;
    private ObjectStorageProperties objectStorageProperties;

    @BeforeEach
    void setUp() {
        storageAuthService = mock(StorageAuthService.class);
        restTemplate = mock(RestTemplate.class);
        objectStorageProperties = mock(ObjectStorageProperties.class);

        service = new ObjectStorageServiceImpl(
                storageAuthService,
                restTemplate,
                objectStorageProperties,
                clock
        );
    }

    @Test
    @DisplayName("파일 업로드 후 파일의 url, 업로드 시간을 반환")
    void fileUpload() throws IOException {
        // given
        when(storageAuthService.getAuthToken()).thenReturn("token");

        HttpMessageConverter messageConverter = mock(HttpMessageConverter.class);
        when(restTemplate.getMessageConverters()).thenReturn(List.of(messageConverter));

        MultipartFile file = mock(MultipartFile.class);

        // when
        FileUploadResponseDto fileUploadResponseDto = service.fileUpload("domain", "type", file);

        // then
        assertThat(fileUploadResponseDto).isNotNull();

        verify(storageAuthService, times(1)).getAuthToken();
        verify(restTemplate, times(1)).getMessageConverters();
        verify(restTemplate, times(1))
                .execute(anyString(), any(), any(), any());
    }

}