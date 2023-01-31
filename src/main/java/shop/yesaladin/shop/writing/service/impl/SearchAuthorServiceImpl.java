package shop.yesaladin.shop.writing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.writing.domain.repository.SearchAuthorRepository;
import shop.yesaladin.shop.writing.dto.SearchAuthorRequestDto;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto;
import shop.yesaladin.shop.writing.service.inter.SearchAuthorService;

/**
 * 엘라스틱서치 작가 검색 구현체
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class SearchAuthorServiceImpl implements SearchAuthorService {

    private final SearchAuthorRepository searchAuthorRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchedAuthorResponseDto searchAuthorByName(SearchAuthorRequestDto searchAuthorRequestDto) {
        return searchAuthorRepository.searchAuthorByName(
                searchAuthorRequestDto.getName(),
                searchAuthorRequestDto.getOffset(),
                searchAuthorRequestDto.getSize()
        );
    }
}
