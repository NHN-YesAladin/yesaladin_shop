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
    NON_MEMBER_ORDER(1), MEMBER_ORDER(2), MEMBER_SUBSCRIBE(3);

    private final int code;
}
