package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.repository.CommandProductTagRepository;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;

/**
 * 태그 관계 등록을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandProductTagServiceImpl implements CommandProductTagService {

    private final CommandProductTagRepository commandProductTagRepository;

    /**
     * 태그 관계를 DB에 등록하고, 등록한 태그 관계 객체를 반환합니다.
     *
     * @param productTag 태그 관계 엔터티
     * @return 등록된 태그 관계 엔터티
     * @author 이수정
     * @since 1.0
     */
    @Override
    public ProductTagResponseDto register(ProductTag productTag) {
        ProductTag savedProductTag = commandProductTagRepository.save(productTag);

        return new ProductTagResponseDto(
                savedProductTag.getPk(),
                savedProductTag.getProduct(),
                savedProductTag.getTag()
        );
    }
}
