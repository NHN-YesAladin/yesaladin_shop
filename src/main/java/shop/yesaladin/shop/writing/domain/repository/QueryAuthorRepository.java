package shop.yesaladin.shop.writing.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.writing.domain.model.Author;

/**
 * 저자 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryAuthorRepository {

    /**
     * Id를 기준으로 저자를 조회합니다.
     *
     * @param id 저자의 Id (PK)
     * @return 조회된 저자 엔터티
     * @author 이수정
     * @since 1.0
     */
    Optional<Author> findById(Long id);

    /**
     * 저자를 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 저자 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    Page<Author> findAllForManager(Pageable pageable);

    /**
     * 저자를 로그인 아이디로 검색
     *
     * @param loginId  검색할 로그인 아이디
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1.0
     */
    Page<Author> findAllByLoginIdForManager(String loginId, Pageable pageable);

    /**
     * 저자를 이름으로 검색
     *
     * @param name     검색할 이름
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1.0
     */
    Page<Author> findAllByNameForManager(String name, Pageable pageable);
}
