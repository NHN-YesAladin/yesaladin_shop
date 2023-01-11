package shop.yesaladin.shop.publish.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.publish.domain.model.Publisher;

/**
 * 출판사 조회 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryPublisherRepository {

    Optional<Publisher> findByName(String name);
}
