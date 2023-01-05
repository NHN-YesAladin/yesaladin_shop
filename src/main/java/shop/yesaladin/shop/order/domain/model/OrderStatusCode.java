package shop.yesaladin.shop.order.domain.model;

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
    REFUND(6);

    private final int statusCode;
}
