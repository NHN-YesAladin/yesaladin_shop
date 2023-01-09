package shop.yesaladin.shop.category.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * 카테고리 조회용(R) 레포지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface QueryCategoryRepository {

    /**
     * 카테고리 list 조회
     *
     * @param pageable size 와 page 를 가진 객체
     * @return paging 되어있는 Category Page 객체
     */
    Page<Category> findAll(Pageable pageable);

    /**
     * id를 통한 카테고리 조회
     *
     * @param id 카테고리 id
     * @return Optional 처리가 된 카테고리 객체
     */
    Optional<Category> findById(Long id);

    /**
     * 카테고리 이름을 통한 카테고리 조회
     *
     * @param name 카테고리 이름
     * @return Optional 처리가 된 카테고리 객체
     */
    Optional<Category> findByName(String name);

}
