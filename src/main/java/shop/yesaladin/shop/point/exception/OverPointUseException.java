package shop.yesaladin.shop.point.exception;

/**
 * 소지한 포인트보다 더 많이 사용하고자 한 경우 발생하는 에러입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public class OverPointUseException extends RuntimeException {

    public OverPointUseException(String loginId, long amount) {
        super("Over Point User: " + loginId + " -> " + amount);
    }
}
