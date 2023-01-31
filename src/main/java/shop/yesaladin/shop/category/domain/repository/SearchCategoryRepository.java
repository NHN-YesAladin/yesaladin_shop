package shop.yesaladin.shop.category.domain.repository;

import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;

/**
 * 엘라스틱서치에서 카테고리를 검색하는 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchCategoryRepository {

    /**
     * 카테고리의 이름으로 검색하는 메서드
     *
     * @param name 검색할 카테고리의 이름
     * @param offset 검색할 페이지 위치
     * @param size 데이터 갯수
     * @return 검색 결과 카테고리 리스트
     */
    SearchCategoryResponseDto searchCategoryByName(String name, int offset, int size);
}
