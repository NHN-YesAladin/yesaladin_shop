package shop.yesaladin.shop.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.file.dto.FileUploadResponseDto;
import shop.yesaladin.shop.file.service.inter.ObjectStorageService;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;

/**
 * 파일 업로드/다운로드를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/files")
public class ObjectStorageController {

    private final ObjectStorageService objectStorageService;
    private final StorageAuthService storageAuthService;

    /**
     * [POST /files/file-upload/{domainName}/{typeName}] 요청받은 파일을 Object Storage에 업로드하고 파일 url을
     * 응답합니다.
     *
     * @param file       업로드할 Multipartfile
     * @param domainName 업로드할 컨테이너 내부의 도메인 명
     * @param typeName   업로드할 파일의 유형명
     * @return 업로드한 파일의 정보
     * @author 이수정
     * @since 1.0
     */
    @PostMapping("/file-upload/{domainName}/{typeName}")
    public ResponseDto<FileUploadResponseDto> fileUpload(
            MultipartFile file,
            @PathVariable String domainName,
            @PathVariable String typeName
    ) {
        return ResponseDto.<FileUploadResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(objectStorageService.fileUpload(domainName, typeName, file))
                .errorMessages(null)
                .build();
    }

    /**
     * [GET /files/auth-token] 오브젝트 스토리지의 토큰을 얻어 반환합니다.
     *
     * @return Object Storage 인증 토큰
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/auth-token")
    public ResponseDto<String> getObjectStorageAuthToken() {
        return ResponseDto.<String>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(storageAuthService.getAuthToken())
                .errorMessages(null)
                .build();
    }
}
