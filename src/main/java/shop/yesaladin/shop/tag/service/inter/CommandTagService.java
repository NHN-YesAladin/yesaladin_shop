package shop.yesaladin.shop.tag.service.inter;

import shop.yesaladin.shop.tag.dto.TagRequestDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;

/**
 * 태그 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandTagService {

    /**
     * 태그를 생성하여 저장하고, 생성된 태그 dto를 반환합니다.
     *
     * @param createDto 생성할 태그의 정보를 가진 Dto
     * @author 이수정
     * @since 1.0
     */
    TagResponseDto create(TagRequestDto createDto);

    /**
     * 태그를 수정하고 수정된 태그 dto를 반환합니다.
     *
     * @param modifyDto 수정된 태그의 정보를 가진 Dto
     * @author 이수정
     * @since 1.0
     */
    TagResponseDto modify(Long tagId, TagRequestDto modifyDto);
}
