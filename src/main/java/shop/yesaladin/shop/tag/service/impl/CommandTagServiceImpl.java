package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandTagRepository;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagRequestDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;

/**
 * 태그 등록을 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandTagServiceImpl implements CommandTagService {

    private final CommandTagRepository commandTagRepository;

    private final QueryTagRepository queryTagRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TagResponseDto create(TagRequestDto createDto) {
        if (queryTagRepository.existsByName(createDto.getName())) {
            throw new ClientException(
                    ErrorCode.TAG_ALREADY_EXIST,
                    "Tag name already exists with name : " + createDto.getName()
            );
        }
        Tag tag = Tag.builder().name(createDto.getName()).build();

        commandTagRepository.save(tag);

        return TagResponseDto.builder().id(tag.getId()).name(tag.getName()).build();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TagResponseDto modify(Long tagId, TagRequestDto modifyDto) {
        Tag tag = queryTagRepository.findById(tagId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.TAG_NOT_FOUND,
                        "Tag is not found with id : " + tagId
                ));

        String modifyName = modifyDto.getName();
        if (!tag.getName().equals(modifyName)) {
            if (queryTagRepository.existsByName(modifyName)) {
                throw new ClientException(
                        ErrorCode.TAG_ALREADY_EXIST,
                        "Tag name already exists with name : " + modifyName
                );
            }
            tag.changeName(modifyName);
        }
        commandTagRepository.save(tag);

        return TagResponseDto.builder().id(tag.getId()).name(tag.getName()).build();
    }
}
