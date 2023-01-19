package shop.yesaladin.shop.point.exception;

/**
 * 포인트 내역 등록 시 유효하지않은 파라미터 값이 들어온 경우의 에러입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public class InvalidCodeParameterException extends RuntimeException {

    public InvalidCodeParameterException(String code) {
        super("Invalid Code Parameter: " + code);
    }
}
