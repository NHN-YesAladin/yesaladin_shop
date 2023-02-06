package shop.yesaladin.shop.order.domain.model;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 주문 상태 코드입니다.
 *
 * @author 최예린
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
    REFUND(6),
    CANCEL(7);

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
}
