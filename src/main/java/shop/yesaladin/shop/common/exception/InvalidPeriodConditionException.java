package shop.yesaladin.shop.common.exception;

import shop.yesaladin.shop.common.exception.type.InvalidPeriodConditionType;

/**
 * 기간 검색 조건이 유효하지 않은 경우(미래의 데이터를 조회하거나 너무 과거의 데이터를 조회하거나 너무 긴 기간의 데이터를 조회하는 등) 발생하는 예외
 *
 * @author 김홍대, 최예린
 * @since 1.0
 */
public class InvalidPeriodConditionException extends RuntimeException {

    private final InvalidPeriodConditionType cause;

    public InvalidPeriodConditionException(InvalidPeriodConditionType cause) {
        super();
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        if (cause.equals(InvalidPeriodConditionType.TOO_PAST)) {
            return "Cannot query with too past date condition";
        } else if (cause.equals(InvalidPeriodConditionType.LONG_PERIOD_OF_TIME)) {
            return "Cannot query with too long period condition";
        } else if(cause.equals(InvalidPeriodConditionType.START_OVER_END)) {
            return "Cannot query with start date over end date period condition";
        }
        return "Cannot query with future date condition";
    }
}
