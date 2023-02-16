package shop.yesaladin.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 매출 통계 정보를 담은 전달하기 위한 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalesStatisticsResponseDto {

    // 상품 ID
    private long id;
    // 상품 제목
    private String title;

    // 주문 건수
    private long numberOfOrders;
    // 주문 개수
    private long totalQuantity;
    // 순매출액
    private String netSales;

    // 주문 취소 건수
    private long numberOfOrderCancellations;
    // 주문 개수
    private long totalCancelQuantity;
    // 주문 취소 금액
    private String cancelSales;
}
