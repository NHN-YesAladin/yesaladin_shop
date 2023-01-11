package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.SubscribeOrderList;

/**
 * 정기구독주문내역 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandSubscribeOrderListRepository {

    /**
     * 정기구독 데이터를 등록합니다.
     *
     * @param subscribeOrderList 월별 정기구독 주문내역
     * @return 등록된 정기구독 데이터
     * @author 최예린
     * @since 1.0
     */
    SubscribeOrderList save(SubscribeOrderList subscribeOrderList);
}
