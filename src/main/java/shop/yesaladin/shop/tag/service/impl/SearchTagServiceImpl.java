package shop.yesaladin.shop.tag.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.tag.domain.repository.SearchTagRepository;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.service.inter.SearchTagService;

/**
 * 태그 검색 서비스 구현체
 *
 * @since : 1.0
 * @author : 김선홍
 */
@RequiredArgsConstructor
@Service
public class SearchTagServiceImpl implements SearchTagService {

    private final SearchTagRepository searchTagRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TagsResponseDto> searchTagByName(String name) {
        return searchTagRepository.searchTagByName(name);
    }
}
