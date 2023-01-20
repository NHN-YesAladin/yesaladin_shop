package shop.yesaladin.shop.writing.service.inter;

import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;

import java.util.List;

/**
 * 저자 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryAuthorService {

    /**
     * ID에 해당하는 저자를 조회하여 Dto로 반환합니다.
     *
     * @param id 저자를 찾아낼 id
     * @return 조회된 저자 dto
     * @author 이수정
     * @since 1.0
     */
    AuthorResponseDto findById(Long id);

    /**
     * 저자를 전체 조회하여 Dto List로 반환합니다.
     *
     * @return 저자 전체 조회한 List
     * @author 이수정
     * @since 1.0
     */
    List<AuthorsResponseDto> findAll();
}