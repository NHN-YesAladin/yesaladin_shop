package shop.yesaladin.shop.category.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;


/**
 * Category 엔티티를 위한 Jpa repository 구현체
 *
 * @author 배수한
 * @since 1.0
 */

public interface JpaCommandCategoryRepository extends Repository<Category, Long>,
        CommandCategoryRepository {


}
