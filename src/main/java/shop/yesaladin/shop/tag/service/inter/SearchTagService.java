package shop.yesaladin.shop.tag.service.inter;

import java.util.List;
import shop.yesaladin.shop.tag.dto.SearchTagRequestDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;

/**
 * 엘라스틱서치 태그 검색 서비스 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchTagService {

    /**
     * 이름으로 태그를 검색하는 메서드
     *
     * @param requestDto 요청 dto
     * @return 검색된 리스트
     */
    SearchedTagResponseDto searchTagByName(SearchTagRequestDto requestDto);
}
