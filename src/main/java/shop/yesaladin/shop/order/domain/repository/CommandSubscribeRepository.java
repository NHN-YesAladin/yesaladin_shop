package shop.yesaladin.shop.order.domain.repository;

import shop.yesaladin.shop.order.domain.model.Subscribe;

/**
 * 정기구주문 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandSubscribeRepository {

    /**
     * 정기구독 데이터를 등록합니다.
     *
     * @param subscribe 정기구독 데이터
     * @return 등록된 정기구독 데이터
     * @author 최예린
     * @since 1.0
     */
    Subscribe save(Subscribe subscribe);
}
