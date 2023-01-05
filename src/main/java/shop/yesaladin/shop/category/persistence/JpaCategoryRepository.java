package shop.yesaladin.shop.category.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * Category 엔티티를 위한 Jpa repository 구현체
 *
 * @author 배수한
 * @since 1.0
 */

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

}
