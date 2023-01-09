package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandTagRepository;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;

/**
 * 태그 등록을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandTagServiceImpl implements CommandTagService {

    private final CommandTagRepository commandTagRepository;

    /**
     * 태그를 DB에 등록하고, 등록한 태그 객체를 반환합니다.
     *
     * @param tag 태그 엔터티
     * @return 등록된 태그 엔터티
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Tag register(Tag tag) {
        return commandTagRepository.save(tag);
    }
}
