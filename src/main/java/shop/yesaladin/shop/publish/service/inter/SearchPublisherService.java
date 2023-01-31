package shop.yesaladin.shop.publish.service.inter;

import shop.yesaladin.shop.publish.dto.SearchPublisherRequestDto;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto;

/**
 * 엘라스틱서치 출판사 검색 서비스 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchPublisherService {

    SearchPublisherResponseDto searchPublisherByName(SearchPublisherRequestDto requestDto);
}
