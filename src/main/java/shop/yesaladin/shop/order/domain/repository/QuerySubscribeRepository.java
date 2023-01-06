package shop.yesaladin.shop.order.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.order.domain.model.Subscribe;

/**
 * 정기구독 조회 관련 repository 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QuerySubscribeRepository {

    /**
     * pk를 통해 정기구독 데이터를 조회합니다.
     *
     * @param id pk
     * @return 조회된 정기구독 데이터
     *
     * @author 최예린
     * @since 1.0
     */
    Optional<Subscribe> findById(Long id);
}
