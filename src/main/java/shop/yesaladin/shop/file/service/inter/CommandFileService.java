package shop.yesaladin.shop.file.service.inter;

import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.dto.FileResponseDto;

/**
 * 파일 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandFileService {

    /**
     * 파일을 DB에 등록하고, 저장한 파일 객체를 반환합니다.
     *
     * @param file 파일 엔터티
     * @return 저장된 파일 객체
     * @author 이수정
     * @since 1.0
     */
    FileResponseDto register(File file);

    FileResponseDto changeFile(long fileId, String fileUrl);
}
