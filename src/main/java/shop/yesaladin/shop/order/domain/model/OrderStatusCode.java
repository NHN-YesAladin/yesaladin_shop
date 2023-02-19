package shop.yesaladin.shop.order.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;

import java.util.Arrays;
import java.util.Optional;

/**
 * 주문 상태 코드입니다.
 *
 * @author 최예린
 * @author 배수한
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum OrderStatusCode {
    ORDER(1),
    DEPOSIT(2),
    READY(3),
    DELIVERY(4),
    COMPLETE(5),
    CONFIRM(6),
    REFUND(7),
    CANCEL(8);

    private final int statusCode;

    /**
     * 상태 문자열을 통해 주문상태코드를 찾아냅니다.
     *
     * @param status 상태
     * @return 주문상태코드
     * @author 최예린
     * @since 1.0
     */
    public static Optional<OrderStatusCode> findByStatus(String status) {
        return Arrays.stream(OrderStatusCode.values())
                .filter(code -> status.equals(code.name()))
                .findFirst();
    }

    /**
     * 상태 코드를 통해 주문상태 코드를 찾아냅니다.
     *
     * @param status status 상태 숫자값
     * @return 주문상태코드
     * @author 배수한
     * @since 1.0
     */
    public static OrderStatusCode getOrderStatusCodeByNumber(Long status) {
        return Arrays.stream(OrderStatusCode.values())
                .filter(c -> c.getStatusCode() == status)
                .findFirst()
                .orElseThrow(() -> new ClientException(
                        ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getDisplayName()));
    }
}
