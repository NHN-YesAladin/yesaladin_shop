package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandTagRepository;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagRequestDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.exception.TagAlreadyExistsException;
import shop.yesaladin.shop.tag.exception.TagNotFoundException;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;

/**
 * 태그 등록을 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandTagServiceImpl implements CommandTagService {

    private final CommandTagRepository commandTagRepository;

    private final QueryTagRepository queryTagRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TagResponseDto register(Tag tag) {
        if (queryTagRepository.existsByName(tag.getName())) {
            throw new TagAlreadyExistsException(tag.getName());
        }
        commandTagRepository.save(tag);

        return new TagResponseDto(tag.getId(), tag.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TagResponseDto create(TagRequestDto createDto) {
        if (queryTagRepository.existsByName(createDto.getName())) {
            throw new TagAlreadyExistsException(createDto.getName());
        }

        Tag tag = Tag.builder().name(createDto.getName()).build();

        commandTagRepository.save(tag);

        return new TagResponseDto(
                tag.getId(),
                tag.getName()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TagResponseDto modify(Long tagId, TagRequestDto modifyDto) {
        Tag tag = queryTagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        String modifyName = modifyDto.getName();
        if (!tag.getName().equals(modifyName)) {
            if (queryTagRepository.existsByName(modifyName)) {
                throw new TagAlreadyExistsException(modifyName);
            }
            tag.changeName(modifyName);
        }

        commandTagRepository.save(tag);

        return new TagResponseDto(
                tag.getId(),
                tag.getName()
        );
    }
}
