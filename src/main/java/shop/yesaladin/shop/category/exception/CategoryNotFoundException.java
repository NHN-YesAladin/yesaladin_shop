package shop.yesaladin.shop.category.exception;

/**
 * 찾고자하는 category 가 없을때 발생하는 예외
 *
 * @author 배수한
 * @since 1.0
 */

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Category not found : " + id);
    }

    public CategoryNotFoundException(String name) {
        super("Category not found : " + name);
    }
}
