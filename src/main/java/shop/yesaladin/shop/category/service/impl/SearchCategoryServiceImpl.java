package shop.yesaladin.shop.category.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.category.domain.repository.SearchCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryRequestDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.SearchCategoryService;

/**
 * 엘라스틱 서치 카테고리 검색 서비스 구현체
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class SearchCategoryServiceImpl implements SearchCategoryService {

    private final SearchCategoryRepository searchCategoryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchCategoryResponseDto searchCategoryByName(SearchCategoryRequestDto searchCategoryRequestDto) {
        return searchCategoryRepository.searchCategoryByName(
                searchCategoryRequestDto.getName(),
                searchCategoryRequestDto.getOffset(),
                searchCategoryRequestDto.getSize()
        );
    }
}
