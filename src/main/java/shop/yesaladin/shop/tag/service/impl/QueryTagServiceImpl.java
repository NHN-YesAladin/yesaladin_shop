package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

import java.util.ArrayList;
import java.util.List;

/**
 * 태그 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryTagServiceImpl implements QueryTagService {

    private final QueryTagRepository queryTagRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public TagResponseDto findById(Long id) {
        Tag tag = queryTagRepository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.TAG_NOT_FOUND,
                        "Tag not found with id : " + id));

        return TagResponseDto.builder().id(tag.getId()).name(tag.getName()).build();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<TagResponseDto> findAllForManager(Pageable pageable) {
        Page<Tag> page = queryTagRepository.findAllForManager(pageable);

        List<TagResponseDto> tags = new ArrayList<>();
        for (Tag tag : page.getContent()) {
            tags.add(TagResponseDto.builder().id(tag.getId()).name(tag.getName()).build());
        }

        return PaginatedResponseDto.<TagResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(tags)
                .build();
    }

}
