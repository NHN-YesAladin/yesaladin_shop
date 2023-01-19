package shop.yesaladin.shop.publish.exception;

/**
 * Id에 맞는 출판사가 없는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class PublisherNotFoundException extends RuntimeException {

    public PublisherNotFoundException(Long id) {
        super("Publisher " + id + "(ID) is not found.");
    }
}
