package shop.yesaladin.shop.publish.service.inter;

import shop.yesaladin.shop.publish.domain.model.Publisher;

/**
 * 출판사 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryPublisherService {

    Publisher findByName(String name);
}
