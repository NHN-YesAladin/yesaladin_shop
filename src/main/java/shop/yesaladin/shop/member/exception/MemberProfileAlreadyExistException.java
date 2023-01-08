package shop.yesaladin.shop.member.exception;

/**
 * member의 loginId 또는 nickname 이 이미 등록 되어 있는 경우 발생 하는 예외
 *
 * @author : 송학현
 * @since : 1.0
 */
public class MemberProfileAlreadyExistException extends RuntimeException {

    public MemberProfileAlreadyExistException(String message) {
        super("Member profile already exists : field = " + message);
    }
}
