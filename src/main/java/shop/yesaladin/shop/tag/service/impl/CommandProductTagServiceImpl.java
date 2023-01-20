package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.repository.CommandProductTagRepository;
import shop.yesaladin.shop.tag.domain.repository.QueryProductTagRepository;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;

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

    /**
     * 태그 관계를 DB에 등록하고, 등록한 태그 관계 Dto를 반환합니다.
     *
     * @param productTag 태그 관계 엔터티
     * @return 등록된 태그 관계 엔터티
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public ProductTagResponseDto register(ProductTag productTag) {
        ProductTag savedProductTag = commandProductTagRepository.save(productTag);

        return new ProductTagResponseDto(
                savedProductTag.getPk(),
                savedProductTag.getProduct(),
                savedProductTag.getTag()
        );
    }

    /**
     * 상품과 관계되어있는 태그 관계를 삭제합니다.
     *
     * @param product 삭제할 태그의 product
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public void deleteByProduct(Product product) {
        if (queryProductTagRepository.existsByProduct(product)) {
            commandProductTagRepository.deleteByProduct(product);
        }
    }
}
