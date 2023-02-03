package shop.yesaladin.shop.order.domain.model;

import java.util.Arrays;
import java.util.Optional;
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
    NON_MEMBER_ORDER(1, NonMemberOrder.class),
    MEMBER_ORDER(2, MemberOrder.class),
    MEMBER_SUBSCRIBE(3, Subscribe.class);

    private final int code;
    private final Class<? extends Order> orderClass;

    /**
     * 타입을 통해 주문 코드를 찾습니다.
     *
     * @param type 타입
     * @return 주문 코드
     * @author 최예린
     * @since 1.0
     */
    public static Optional<OrderCode> findByType(String type) {
        return Arrays.stream(OrderCode.values())
                .filter(code -> type.toUpperCase().equals(code.name()))
                .findFirst();
    }
}
