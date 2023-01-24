package shop.yesaladin.shop.member.exception;

/**
 * 차단 해지된 회원을 다시 차단 해지 시킬때 발생하는 예외입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public class AlreadyUnblockedMemberException extends RuntimeException {

    public AlreadyUnblockedMemberException(String loginId) {
        super("Already Unblocked Member: " + loginId);
    }
}
