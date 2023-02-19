package shop.yesaladin.shop.publish.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.publish.domain.model.Publisher;

import java.util.List;
import java.util.Optional;

/**
 * 출판사 조회 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryPublisherRepository {

    /**
     * Id를 기준으로 출판사를 조회합니다.
     *
     * @param id 출판사의 Id (PK)
     * @return 조회된 출판사 엔터티
     * @author 이수정
     * @since 1.0
     */
    Optional<Publisher> findById(Long id);

    /**
     * 출판사를 전체 조회합니다.
     *
     * @return 조회된 출판사 엔터티 List
     * @author 이수정
     * @since 1.0
     */
    List<Publisher> findAll();

    /**
     * 출판사를 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 출판사 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    Page<Publisher> findAllForManager(Pageable pageable);

    /**
     * 출판사를 이름으로 검색하는 메서드
     *
     * @param name 검색할 이름
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1.0
     */
    Page<Publisher> findByNameForManager(String name, Pageable pageable);

    /**
     * 이미 존재하는 출판사 이름인지 확인합니다.
     *
     * @return 확인할 출판사 이름
     * @author 이수정
     * @since 1.0
     */
    boolean existsByName(String name);
}
