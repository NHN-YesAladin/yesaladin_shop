package shop.yesaladin.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MyBatis 에서 조회한 매출 통계 정보를 담은 Dto 입니다.
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
    // 상품 제목
    private String title;

    // 주문 건수
    private long numberOfOrders;
    // 주문 개수
    private long totalQuantity;

    // 판매가 관련
    private long actualPrice;
    private int discountRate;

    // 주문 취소 건수
    private long numberOfOrderCancellations;
    // 주문 개수
    private long totalCancelQuantity;
}
