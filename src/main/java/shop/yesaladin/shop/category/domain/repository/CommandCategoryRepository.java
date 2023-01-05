package shop.yesaladin.shop.category.domain.repository;

import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.category.domain.model.Category;

public interface CommandCategoryRepository {

    Category save(Category category);
}
