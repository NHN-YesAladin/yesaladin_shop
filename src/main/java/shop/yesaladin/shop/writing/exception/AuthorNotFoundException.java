package shop.yesaladin.shop.writing.exception;

/**
 * ID에 맞는 저자가 없는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(Long id) {
        super("Author " + id + "(ID) is not found.");
    }
}
