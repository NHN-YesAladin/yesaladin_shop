package shop.yesaladin.shop.tag.service.inter;

import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.dto.TagResponseDto;

/**
 * 태그 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandTagService {

    /**
     * 태그를 DB에 등록하고, 등록한 태그 객체를 반환합니다.
     *
     * @param tag 태그 엔터티
     * @return 등록된 태그 엔터티
     * @author 이수정
     * @since 1.0
     */
    TagResponseDto register(Tag tag);
}
