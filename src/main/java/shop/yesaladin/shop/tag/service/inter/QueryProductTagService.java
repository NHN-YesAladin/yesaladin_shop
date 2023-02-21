package shop.yesaladin.shop.tag.service.inter;

import java.util.List;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;

/**
 * 태그 관계 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryProductTagService {

    /**
     * 해당 상품의 태그 관계를 조회하고, Dto List로 반환합니다.
     *
     * @param product 관계를 조회할 상품
     * @return 조회된 태그 관계 dto List
     * @author 이수정
     * @since 1.0
     */
    List<ProductTagResponseDto> findByProduct(Product product);
}
