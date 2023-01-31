package shop.yesaladin.shop.point.exception;

import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.shop.common.exception.CommonException;

/**
 * 소지한 포인트보다 더 많이 사용하고자 한 경우 발생하는 에러입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public class OverPointUseException extends CommonException {

    public OverPointUseException() {
        super(ErrorCode.POINT_OVER_USE);
    }
}
