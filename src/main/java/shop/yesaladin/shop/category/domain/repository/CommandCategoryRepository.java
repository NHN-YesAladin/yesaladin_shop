package shop.yesaladin.shop.category.domain.repository;

import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.category.domain.model.Category;

/**
 * CUD용 레포지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface CommandCategoryRepository {

    Category save(Category category);

    void deleteById(Long id);
}
