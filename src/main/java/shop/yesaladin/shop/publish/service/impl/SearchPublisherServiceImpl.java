package shop.yesaladin.shop.publish.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryRequestDto;
import shop.yesaladin.shop.category.service.inter.SearchCategoryService;
import shop.yesaladin.shop.publish.domain.repository.SearchPublisherRepository;
import shop.yesaladin.shop.publish.dto.SearchPublisherRequestDto;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.SearchPublisherService;

/**
 * 엘라스틱서치 출판사 검색 서비스 구현체
 *
 * @since : 1.0
 * @author : 김선홍
 */
@RequiredArgsConstructor
@Service
public class SearchPublisherServiceImpl implements SearchPublisherService {

    private final SearchPublisherRepository searchPublisherRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchPublisherResponseDto searchPublisherByName(SearchPublisherRequestDto requestDto) {
        return searchPublisherRepository.searchPublisherByName(requestDto.getName(),
                requestDto.getOffset(), requestDto.getSize()
        );
    }
}
