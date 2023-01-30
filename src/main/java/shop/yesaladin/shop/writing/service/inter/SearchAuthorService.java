package shop.yesaladin.shop.writing.service.inter;

import shop.yesaladin.shop.writing.dto.SearchAuthorRequestDto;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto;

/**
 * 엘라스틱서치 작가 검색 서비스 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchAuthorService {

    SearchedAuthorResponseDto searchAuthorByName(SearchAuthorRequestDto searchAuthorRequestDto);
}
