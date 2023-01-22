package shop.yesaladin.shop.file.service.inter;

import shop.yesaladin.shop.file.dto.FileResponseDto;

/**
 * 파일 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryFileService {

    /**
     * Id로 파일을 조회해 반환합니다.
     *
     * @param id 찾고자하는 파일의 Id
     * @return 찾은 파일 엔터티
     * @author 이수정
     * @since 1.0
     */
    FileResponseDto findById(Long id);
}
