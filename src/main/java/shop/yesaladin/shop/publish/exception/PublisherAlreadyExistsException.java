package shop.yesaladin.shop.publish.exception;

/**
 * 이미 존재하는 출판사인 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class PublisherAlreadyExistsException extends RuntimeException {
    public PublisherAlreadyExistsException(String name) {
        super("Publisher " + name + "(name) is already exists.");
    }
}
