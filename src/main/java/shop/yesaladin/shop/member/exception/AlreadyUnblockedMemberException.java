package shop.yesaladin.shop.member.exception;

public class AlreadyUnblockedMemberException extends RuntimeException {

    public AlreadyUnblockedMemberException(Long id) {
        super("Already Unblocked Member: " + id);
    }
}
