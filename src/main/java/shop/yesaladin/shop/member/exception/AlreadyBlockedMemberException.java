package shop.yesaladin.shop.member.exception;

/**
 * 이미 차단된 회원을 다시 차단할 때 발생하는 예외입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public class AlreadyBlockedMemberException extends RuntimeException {

    public AlreadyBlockedMemberException(String loginId) {
        super("Already Blocked Member: " + loginId);
    }
}
