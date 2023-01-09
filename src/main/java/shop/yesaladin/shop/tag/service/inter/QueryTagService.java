package shop.yesaladin.shop.tag.service.inter;

import shop.yesaladin.shop.tag.domain.model.Tag;

/**
 * 태그 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryTagService {

    Tag findByName(String name);
}
