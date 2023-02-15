package shop.yesaladin.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 매출 통계 정보를 담은 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesStatisticsMyBatisResponseDto {

    // 상품 ID
    private long id;
    // 주문 건수
    private long numberOfOrders;
    // 주문 개수
    private long totalQuantity;

    // 실구매가
    private String creditCardSales;
    // 판매가 관련
    private long actualPrice;
    private long discountRate;

    // 주문 취소 건수
    private long numberOfOrderCancellations;
    // 주문 개수
    private long totalCancelQuantity;
    // 주문 취소 금액
    private long cancelSales;
}
