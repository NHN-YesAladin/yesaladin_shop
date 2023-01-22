package shop.yesaladin.shop.file.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.shop.file.dto.FileUploadResponseDto;
import shop.yesaladin.shop.file.service.inter.FileStorageService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Object Storage 파일 업로드/다운로드를 하기 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${storage-token.storage-url}")
    private String storageUrl;
    @Value("${storage-token.storage-account}")
    private String storageAccount;
    @Value("${storage-token.container-name}")
    private String containerName;

    private RestTemplate restTemplate;

    /**
     * 파일을 저장할 url을 만들어 반환합니다.
     *
     * @param domainName 파일을 저장할 컨테이너 내의 도메인 경로
     * @param typeName   파일을 저장할 도메인 내의 파일 유형 경로
     * @param fileName   저장할 파일의 uuid.확장자 형태의 이름
     * @return 파일을 저장할 url
     * @author 이수정
     * @since 1.0
     */
    private String getUrl(
            @NonNull String domainName,
            @NonNull String typeName,
            @NonNull String fileName
    ) {
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder
                .append(storageUrl).append("/")
                .append(storageAccount).append("/")
                .append(containerName).append("/")
                .append(domainName).append("/")
                .append(typeName).append("/")
                .append(fileName).toString();
    }

    /**
     * 요청받은 파일을 업로드하고 파일의 url과 업로드 시간을 반환합니다.
     *
     * @param token      오브젝트 스토리지 인증 토큰
     * @param domainName 파일을 저장할 컨테이너 내의 도메인 경로
     * @param typeName   파일을 저장할 도메인 내의 파일 유형 경로
     * @param file       요청받은 파일
     * @return 저장된 파일의 정보를 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @Override
    public FileUploadResponseDto fileUpload(
            String token,
            String domainName,
            String typeName,
            MultipartFile file
    ) {

        // InputStream을 요청 본문에 추가할 수 있도록 RequestCallback 오버라이드
        final RequestCallback requestCallback = new RequestCallback() {
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                request.getHeaders().add("X-Auth-Token", token);
                IOUtils.copy(file.getInputStream(), request.getBody());
            }
        };

        // 오버라이드한 RequestCallback을 사용할 수 있도록 설정
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        restTemplate = new RestTemplate(requestFactory);

        HttpMessageConverterExtractor<String> responseExtractor
                = new HttpMessageConverterExtractor<String>(
                String.class,
                restTemplate.getMessageConverters()
        );

        long start = System.currentTimeMillis();
        log.info("start : " + start);

        // API 호출
        String filename = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String url = getUrl(domainName, typeName, filename);
        restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);

        long end = System.currentTimeMillis();
        log.info("end : " + end);

        log.info("upload end : " + (end - start));
        return new FileUploadResponseDto(url, LocalDateTime.now().toString());
    }
}
