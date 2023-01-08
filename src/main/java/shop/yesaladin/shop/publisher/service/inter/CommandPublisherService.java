package shop.yesaladin.shop.publisher.service.inter;

import shop.yesaladin.shop.publisher.domain.model.Publisher;

/**
 * 출판사 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandPublisherService {

    Publisher register(Publisher publisher);
}
