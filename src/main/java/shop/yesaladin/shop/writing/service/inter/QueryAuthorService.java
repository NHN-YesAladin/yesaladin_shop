package shop.yesaladin.shop.writing.service.inter;

import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;

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
     * 페이징된 관리자용 저자 리스트를 반환합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 조회된 저자 dto를 담은 객체
     * @author 이수정
     * @since 1.0
     */
    PaginatedResponseDto<AuthorsResponseDto> findAllForManager(Pageable pageable);

    /**
     * 저자를 로그인 아이디로 검색
     *
     * @param loginId  검색할 로그인 아이디
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1.0
     */
    PaginatedResponseDto<AuthorsResponseDto> findAllByLoginIdForManager(
            String loginId,
            Pageable pageable
    );

    /**
     * 저자를 이름으로 검색
     *
     * @param name     검색할 이름
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1.0
     */
    PaginatedResponseDto<AuthorsResponseDto> findAllByNameForManager(
            String name,
            Pageable pageable
    );
}
