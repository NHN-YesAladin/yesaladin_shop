package shop.yesaladin.shop.tag.exception;

/**
 * ID에 맞는 태그가 없는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(Long id) {
        super("Tag " + id + "(ID) is not found.");
    }
}
