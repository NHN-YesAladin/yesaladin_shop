package shop.yesaladin.shop.category.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(long id) {
        super("Category not found : " + id);
    }
}
