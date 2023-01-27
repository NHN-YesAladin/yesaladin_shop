package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dto.ProductTagCreateDto;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandProductTagRepository;
import shop.yesaladin.shop.tag.domain.repository.QueryProductTagRepository;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

/**
 * 태그 관계 등록을 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandProductTagServiceImpl implements CommandProductTagService {

    private final CommandProductTagRepository commandProductTagRepository;
    private final QueryProductTagRepository queryProductTagRepository;
    private final QueryTagService queryTagService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ProductTagResponseDto register(ProductTagCreateDto createDto) {
        Tag tag = queryTagService.findById(createDto.getTagId()).toEntity();
        ProductTag savedProductTag = commandProductTagRepository.save(ProductTag.create(
                createDto.getProduct(),
                tag
        ));

        return new ProductTagResponseDto(
                savedProductTag.getPk(),
                savedProductTag.getProduct(),
                savedProductTag.getTag()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteByProduct(Product product) {
        if (queryProductTagRepository.existsByProduct(product)) {
            commandProductTagRepository.deleteByProduct(product);
        }
    }
}
