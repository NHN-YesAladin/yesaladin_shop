package shop.yesaladin.shop.member.exception;

public class AlreadyBlockedMemberException extends RuntimeException {

    public AlreadyBlockedMemberException(Long id) {
        super("Already Blocked Member: " + id);
    }
}
