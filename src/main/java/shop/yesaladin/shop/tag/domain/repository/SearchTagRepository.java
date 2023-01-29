package shop.yesaladin.shop.tag.domain.repository;

import java.util.List;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;

/**
 * 엘라스틱 서치 태그 검색 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchTagRepository {

    /**
     * 태그의 이름으로 태그를 검색한다.
     *
     * @param name 검색할 태그의 이름
     * @return 검색된 태그들
     * @author : 김선홍
     * @since : 1.0
     */
    List<TagsResponseDto> searchTagByName(String name);
}
