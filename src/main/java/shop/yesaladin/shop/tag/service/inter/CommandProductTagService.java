package shop.yesaladin.shop.tag.service.inter;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dto.ProductTagCreateDto;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;

/**
 * 태그 관계 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandProductTagService {

    /**
     * 태그 관계를 DB에 등록하고, 등록한 태그 관계 Dto를 반환합니다.
     *
     * @param createDto 태그 관계 생성 DTO
     * @return 등록된 태그 관계 엔터티
     * @author 이수정
     * @since 1.0
     */
    ProductTagResponseDto register(ProductTagCreateDto createDto);

    /**
     * 상품과 관계되어있는 태그 관계를 삭제합니다.
     *
     * @param product 삭제할 태그의 product
     * @author 이수정
     * @since 1.0
     */
    void deleteByProduct(Product product);
}
