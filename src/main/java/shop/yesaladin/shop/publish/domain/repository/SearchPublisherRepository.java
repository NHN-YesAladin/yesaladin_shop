package shop.yesaladin.shop.publish.domain.repository;

import java.util.List;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto;

/**
 * 엘라스틱서치 출판사 검색 레포지토리 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchPublisherRepository {
    SearchPublisherResponseDto searchPublisherByName(String name, int offset, int size);
}
