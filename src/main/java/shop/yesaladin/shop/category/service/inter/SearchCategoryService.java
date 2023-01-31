package shop.yesaladin.shop.category.service.inter;

import java.util.List;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryRequestDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;

/**
 * 엘라스틱서치 카테고리 검색 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchCategoryService {

    /**
     * 카테고리 이름으로 검색하는 메서드
     *
     * @param searchCategoryRequestDto 카테고리 이름, 페이지 위치, 데이터 갯수
     * @return 검색된 카테고리 리스트와 총 갯수
     */
    SearchCategoryResponseDto searchCategoryByName(SearchCategoryRequestDto searchCategoryRequestDto);
}
