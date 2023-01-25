package shop.yesaladin.shop.file.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.shop.file.dto.FileUploadResponseDto;
import shop.yesaladin.shop.file.service.inter.FileStorageService;

/**
 * 파일 업로드/다운로드를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/v1/files")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    /**
     * 요청받은 파일을 Object Storage에 업로드하고 파일 url을 응답합니다.
     *
     * @param file       업로드할 Multipartfile
     * @param domainName 업로드할 컨테이너 내부의 도메인 명
     * @param typeName   업로드할 파일의 유형명
     * @return 업로드한 파일의 정보
     * @author 이수정
     * @since 1.0
     */
    @PostMapping("/file-upload/{domainName}/{typeName}")
    public ResponseEntity fileUpload(
            MultipartFile file,
            @PathVariable String domainName,
            @PathVariable String typeName
    ) {
        FileUploadResponseDto responseDto = fileStorageService.fileUpload(
                domainName,
                typeName,
                file
        );

        return ResponseEntity.ok().body(responseDto);
    }
}
