package shop.yesaladin.shop.publish.domain.repository;

import shop.yesaladin.shop.publish.domain.model.Publisher;

/**
 * 출판사 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandPublisherRepository {

    Publisher save(Publisher publisher);
}
