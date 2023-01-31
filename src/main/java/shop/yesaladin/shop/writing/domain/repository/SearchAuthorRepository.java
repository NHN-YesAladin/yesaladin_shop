package shop.yesaladin.shop.writing.domain.repository;

import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto;

/**
 * 엘라스틱서치 저자 검색 레포지토리 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchAuthorRepository {

    /**
     * 저자의 이름으로 저자를 검색하는 메서드
     *
     * @param name 검색할 저자의 이름
     * @param offset 페이지 위치
     * @param size 데이터 갯수
     * @return 검색된 저자리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    SearchedAuthorResponseDto searchAuthorByName(String name, int offset, int size);
}
