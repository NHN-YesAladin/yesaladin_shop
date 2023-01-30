package shop.yesaladin.shop.writing.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 페이징된 관리자용 저자 리스트를 반환합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 조회된 저자 dto를 담은 객체
     * @author 이수정
     * @since 1.0
     */
    Page<AuthorsResponseDto> findAllForManager(Pageable pageable);
}
