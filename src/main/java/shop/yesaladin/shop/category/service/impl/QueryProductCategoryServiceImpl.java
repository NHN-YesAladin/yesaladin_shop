package shop.yesaladin.shop.category.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.category.domain.repository.QueryProductCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.QueryProductCategoryService;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품 카테고리 조회 서비스 구현 클래스
 *
 * @author 배수한
 * @since 1.0
 */

@RequiredArgsConstructor
@Service
public class QueryProductCategoryServiceImpl implements QueryProductCategoryService {

    private final QueryProductCategoryRepository repository;

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public List<CategoryResponseDto> findCategoriesByProduct(Product product) {
        return repository.findCategoriesByProduct(product);
    }
}
