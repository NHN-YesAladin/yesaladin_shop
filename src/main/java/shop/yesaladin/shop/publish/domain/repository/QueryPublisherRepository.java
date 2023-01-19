package shop.yesaladin.shop.publish.domain.repository;

import shop.yesaladin.shop.publish.domain.model.Publisher;

import java.util.List;
import java.util.Optional;

/**
 * 출판사 조회 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryPublisherRepository {

    Optional<Publisher> findById(Long id);

    Optional<Publisher> findByName(String name);
}
