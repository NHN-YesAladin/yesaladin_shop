package shop.yesaladin.shop.point.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 포인트 사유 코드 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum PointReasonCode {
    SAVE_ORDER(1, "상품 구매"),
    SAVE_ORDER_CANCEL(2, "상품 취소로 인한 포인트 환불"),
    SAVE_ORDER_REFUND(3, "상품 환불로 인한 포인트 환불"),
    SAVE_COUPON(4, "포인트 쿠폰 사용"),
    SAVE_PRESENT(5, "선물 받은 포인트"),
    USE_ORDER(6, "주문에 사용"),
    USE_PRESENT(7, "포인트 선물"),
    SUM(8, "포인트 집계");

    private final int id;
    private final String reason;
}
