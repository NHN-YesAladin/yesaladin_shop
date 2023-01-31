package shop.yesaladin.shop.tag.exception;

/**
 * 이미 존재하는 태그인 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class TagAlreadyExistsException extends RuntimeException {
    public TagAlreadyExistsException(String name) {
        super("Tag " + name + "(name) is already exists.");
    }
}
