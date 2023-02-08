package shop.yesaladin.shop.file.service.inter;

import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.shop.file.dto.FileUploadResponseDto;

/**
 * 파일 업로드/다운로드를 하기 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface ObjectStorageService {

    /**
     * 요청받은 파일을 업로드하고 파일의 url과 업로드 시간을 반환합니다.
     *
     * @param domainName 파일을 저장할 컨테이너 내의 도메인 경로
     * @param typeName   파일을 저장할 도메인 내의 파일 유형 경로
     * @param file       요청받은 파일
     * @return 저장된 파일의 정보를 담은 dto
     * @author 이수정
     * @since 1.0
     */
    FileUploadResponseDto fileUpload(
            String domainName,
            String typeName,
            MultipartFile file
    );
}
