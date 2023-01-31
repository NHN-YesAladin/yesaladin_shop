package shop.yesaladin.shop.writing.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.writing.domain.model.Author;

import java.util.List;
import java.util.Optional;

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
     * 저자를 전체 조회합니다.
     *
     * @return 조회된 저자 엔터티 List
     * @author 이수정
     * @since 1.0
     */
    List<Author> findAll();

    /**
     * 저자를 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 저자 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    Page<Author> findAllForManager(Pageable pageable);
}
