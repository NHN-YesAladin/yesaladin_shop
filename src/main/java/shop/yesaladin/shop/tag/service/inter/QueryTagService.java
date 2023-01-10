package shop.yesaladin.shop.tag.service.inter;

import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.dto.TagResponseDto;

/**
 * 태그 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryTagService {

    TagResponseDto findByName(String name);
}
