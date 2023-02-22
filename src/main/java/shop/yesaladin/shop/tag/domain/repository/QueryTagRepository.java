package shop.yesaladin.shop.tag.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.tag.domain.model.Tag;

/**
 * 태그 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryTagRepository {

    /**
     * Id를 기준으로 태그를 조회합니다.
     *
     * @param id 태그의 Id (PK)
     * @return 조회된 태그 엔터티
     * @author 이수정
     * @since 1.0
     */
    Optional<Tag> findById(Long id);

    /**
     * 태그를 전체 조회합니다.
     *
     * @return 조회된 태그 엔터티 List
     * @author 이수정
     * @since 1.0
     */
    List<Tag> findAll();

    /**
     * 태그를 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 태그 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    Page<Tag> findAllForManager(Pageable pageable);

    /**
     * 태그를 이름으로 검색하는 메서드
     *
     * @param name     검색할 이름
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1.0
     */
    Page<Tag> findByNameForManager(String name, Pageable pageable);

    /**
     * 이미 존재하는 태그 이름인지 확인합니다.
     *
     * @return 확인할 태그 이름
     * @author 이수정
     * @since 1.0
     */
    boolean existsByName(String name);
}
