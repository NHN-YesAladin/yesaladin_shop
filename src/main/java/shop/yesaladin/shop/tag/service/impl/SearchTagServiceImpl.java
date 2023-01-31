package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.tag.domain.repository.SearchTagRepository;
import shop.yesaladin.shop.tag.dto.SearchTagRequestDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.SearchTagService;

/**
 * 태그 검색 서비스 구현체
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class SearchTagServiceImpl implements SearchTagService {

    private final SearchTagRepository searchTagRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchedTagResponseDto searchTagByName(SearchTagRequestDto requestDto) {
        return searchTagRepository.searchTagByName(requestDto.getName(), requestDto.getOffset(),
                requestDto.getSize()
        );
    }
}
