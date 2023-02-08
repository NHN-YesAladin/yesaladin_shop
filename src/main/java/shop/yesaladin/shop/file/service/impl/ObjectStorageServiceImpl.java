package shop.yesaladin.shop.file.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.shop.config.ObjectStorageProperties;
import shop.yesaladin.shop.file.dto.FileUploadResponseDto;
import shop.yesaladin.shop.file.service.inter.ObjectStorageService;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Object Storage 파일 업로드/다운로드를 하기 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class ObjectStorageServiceImpl implements ObjectStorageService {

    private final StorageAuthService storageAuthService;

    private final RestTemplate restTemplate;
    private final ObjectStorageProperties objectStorage;
    private final Clock clock;

    /**
     * {@inheritDoc}
     */
    private String getUrl(
            @NonNull String domainName,
            @NonNull String typeName,
            @NonNull String fileName
    ) {
        return objectStorage.getStorageUrl() + "/" +
                objectStorage.getStorageAccount() + "/" +
                objectStorage.getContainerName() + "/" +
                domainName + "/" +
                typeName + "/" +
                fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileUploadResponseDto fileUpload(
            String domainName,
            String typeName,
            MultipartFile file
    ) {
        String token = storageAuthService.getAuthToken();

        // InputStream을 요청 본문에 추가할 수 있도록 RequestCallback 오버라이드
        final RequestCallback requestCallback = request -> {
            request.getHeaders().add("X-Auth-Token", token);
            IOUtils.copy(file.getInputStream(), request.getBody());
        };

        HttpMessageConverterExtractor<String> responseExtractor
                = new HttpMessageConverterExtractor<>(
                String.class,
                restTemplate.getMessageConverters()
        );

        // API 호출
        String filename = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String url = getUrl(domainName, typeName, filename);
        restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);

        return new FileUploadResponseDto(url, LocalDateTime.now(clock).toString());
    }
}
