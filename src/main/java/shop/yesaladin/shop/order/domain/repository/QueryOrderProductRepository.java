package shop.yesaladin.shop.order.domain.repository;

import java.util.List;
import shop.yesaladin.shop.order.dto.OrderProductResponseDto;

/**
 * 주문 상품의 조회 관련 repository interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryOrderProductRepository {

    /**
     * 주문 번호를 통해 해당하는 모든 주문 상품을 조회합니다.
     *
     * @param orderNumber 조회하고자 하는 주문 번호
     * @return 상품 정보
     */
    List<OrderProductResponseDto> findAllByOrderNumber(String orderNumber);
}
