package shop.yesaladin.shop.order.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 주문 코드 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum OrderCode {
    NON_MEMBER_ORDER(1, NonMemberOrder.class), MEMBER_ORDER(2, MemberOrder.class),
    MEMBER_SUBSCRIBE(3, SubscribeOrder.class);

    private final int code;
    private final Class<? extends Order> orderClass;
}
