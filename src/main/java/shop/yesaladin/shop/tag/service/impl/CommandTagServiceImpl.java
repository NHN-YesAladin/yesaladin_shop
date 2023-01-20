package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandTagRepository;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
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

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public TagResponseDto register(Tag tag) {
        Tag savedTag = commandTagRepository.save(tag);

        return new TagResponseDto(savedTag.getId(), savedTag.getName());
    }
}
