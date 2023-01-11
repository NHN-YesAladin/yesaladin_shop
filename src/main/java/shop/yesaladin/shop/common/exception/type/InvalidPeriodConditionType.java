package shop.yesaladin.shop.common.exception.type;

/**
 * InvalidPeriodConditionException에서 어떤 이유로 조건이 유효하지 않은지 판단하기 위한 enum 클래스
 *
 * @author 김홍대, 최예린
 * @see shop.yesaladin.shop.common.exception.InvalidPeriodConditionException
 * @since 1.0
 */
public enum InvalidPeriodConditionType {
    TOO_PAST, LONG_PERIOD_OF_TIME, FUTURE, INVALID
}
