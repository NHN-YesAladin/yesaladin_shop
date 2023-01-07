package shop.yesaladin.shop.category.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * 조회용(R) 레포지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface QueryCategoryRepository {

    Page<Category> findAll(Pageable pageable);

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);

    //TODO N+1 문제 발생으로 인한 Querydsl 사용 필요 확인
    List<Category> findByParent_Name(String parent);
}
