package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.exception.TagNotFoundException;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 태그 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QueryTagServiceImpl implements QueryTagService {

    private final QueryTagRepository queryTagRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public TagResponseDto findById(Long id) {
        Tag tag = queryTagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        return new TagResponseDto(tag.getId(), tag.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public TagResponseDto findByName(String name) {
        Tag tag = queryTagRepository.findByName(name).orElse(null);

        if (Objects.isNull(tag)) {
            return null;
        }
        return new TagResponseDto(tag.getId(), tag.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<TagsResponseDto> findAll() {
        List<Tag> tags = queryTagRepository.findAll();

        return tags.stream()
                .map(tag -> new TagsResponseDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

}
