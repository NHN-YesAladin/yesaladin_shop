package shop.yesaladin.shop.category.exception;

/**
 * 이미 삭제된 카테고리를 삭제 할 경우 발생하는 예외
 *
 * @author 배수한
 * @since 1.0
 */
public class AlreadyDeletedCategoryException extends RuntimeException{

    public AlreadyDeletedCategoryException(Long id) {
        super("Already deleted category : " + id);
    }
}
