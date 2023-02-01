package shop.yesaladin.shop.order.domain.repository;

/**
 * 주문 상품 조회 관련 repository 클래스입니다.
 *
 * @author 배수한
 * @since 1.0
 */
public interface QueryOrderProductRepository {

    /**
     * 특정 주문의 주문 상품의 개수를 조회하는 메서드 입니다.
     *
     * @param orderId
     * @return
     */
    Long getCountOfOrderProductByOrderId(Long orderId);
}
