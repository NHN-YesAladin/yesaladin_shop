package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

/**
 * 태그 조회를 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QueryTagServiceImpl implements QueryTagService {

    private final QueryTagRepository queryTagRepository;

    /**
     * 태그명으로 이미 저장되어있는 태그인지 확인하고, 존재한다면 태그 엔터티를 반환, 존재하지 않는다면 null을 반환합니다.
     *
     * @param name 찾고자하는 태그의 태그명
     * @return 찾은 태그 엔터티 or null
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Tag findByName(String name) {
        return queryTagRepository.findByName(name).orElse(null);
    }
}
