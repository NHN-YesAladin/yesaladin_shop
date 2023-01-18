package shop.yesaladin.shop.point.exception;

public class OverPointUseException extends RuntimeException {

    public OverPointUseException(long memberId, long amount) {
        super("Over Point User: " + memberId + " -> " + amount);
    }
}
