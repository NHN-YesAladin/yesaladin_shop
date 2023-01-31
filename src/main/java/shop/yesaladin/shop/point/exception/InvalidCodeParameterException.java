package shop.yesaladin.shop.point.exception;

import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.shop.common.exception.CommonException;

/**
 * 포인트 내역 등록 시 유효하지않은 파라미터 값이 들어온 경우의 에러입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public class InvalidCodeParameterException extends CommonException {

    public InvalidCodeParameterException() {
        super(ErrorCode.POINT_INVALID_PARAMETER);
    }
}
