package shop.yesaladin.shop.tag.service.inter;

import java.util.List;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;

public interface SearchTagService {
    List<TagsResponseDto> searchTagByName(String name);
}
