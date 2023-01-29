package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.exception.TagNotFoundException;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

import java.util.ArrayList;
import java.util.List;
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
    public List<TagsResponseDto> findAll() {
        return queryTagRepository.findAll().stream()
                .map(tag -> new TagsResponseDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<TagsResponseDto> findAllForManager(Pageable pageable) {
        Page<Tag> page = queryTagRepository.findAllForManager(pageable);

        List<TagsResponseDto> tags = new ArrayList<>();
        for (Tag tag : page.getContent()) {
            tags.add(new TagsResponseDto(
                    tag.getId(),
                    tag.getName()
            ));
        }

        return new PageImpl<>(tags, pageable, page.getTotalElements());
    }

}
