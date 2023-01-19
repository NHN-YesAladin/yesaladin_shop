package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.exception.TagNotFoundException;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

import java.util.Objects;

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
     * Id에 해당하는 태그를 조회하여 Dto로 반환합니다.
     *
     * @param id 태그를 찾아낼 id
     * @return 조회된 태그 dto
     * @author 이수정
     * @since 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public TagResponseDto findById(Long id) {
        Tag tag = queryTagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        return new TagResponseDto(tag.getId(), tag.getName());
    }

    /**
     * 태그명으로 이미 저장되어있는 태그인지 확인하고, 존재한다면 태그 Dto를 반환, 존재하지 않는다면 null을 반환합니다.
     *
     * @param name 찾고자하는 태그의 태그명
     * @return 찾은 태그 dto or null
     * @author 이수정
     * @since 1.0
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

}
