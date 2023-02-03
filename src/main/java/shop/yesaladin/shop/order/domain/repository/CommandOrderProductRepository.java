package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.OrderProduct;

/**
 * 주문 상품 생성/수정/삭제 관련 repository interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandOrderProductRepository {

    /**
     * 주문 상품을 등록합니다.
     *
     * @param request 주문 상품 데이터
     * @return 등록된 주문 상품
     * @author 최예린
     * @since 1.0
     */
    OrderProduct save(OrderProduct request);

}
