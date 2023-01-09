package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

/**
 * 출판사 조회를 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QueryPublisherServiceImpl implements QueryPublisherService {

    private final QueryPublisherRepository queryPublisherRepository;

    /**
     * 출판사 이름으로 이미 저장되어있는 출판사인지 확인하고, 존재한다면 출판사 엔터티를 반환, 존재하지 않는다면 null을 반환합니다.
     *
     * @param name 찾고자하는 출판사 이름
     * @return 찾은 출판사 엔터티 or null
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Publisher findByName(String name) {
        return queryPublisherRepository.findByName(name).orElse(null);
    }
}
