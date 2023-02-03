package shop.yesaladin.shop.category.service.inter;

import java.util.List;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품 카테고리 조회용 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */
public interface QueryProductCategoryService {

    /**
     * 특정 상품을 통해 카테고리 리스트를 받아오는 메서드
     *
     * @param product 찾고자하는 상품
     * @return 카테고리 정보 리스트
     */
    List<CategoryResponseDto> findCategoriesByProduct(Product product);
}
