package shop.yesaladin.shop.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;
import shop.yesaladin.shop.category.domain.repository.CommandProductCategoryRepository;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.ProductCategoryIdDto;
import shop.yesaladin.shop.category.dto.ProductCategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.CommandProductCategoryService;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * @author 배수한
 * @since 1.0
 */

@RequiredArgsConstructor
@Service
public class CommandProductCategoryServiceImpl implements CommandProductCategoryService {

    private final CommandProductCategoryRepository commandProductCategoryRepository;

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public ProductCategoryResponseDto register(ProductCategory productCategory) {
        ProductCategory foundProductCategory = commandProductCategoryRepository.save(
                productCategory);
        return ProductCategoryResponseDto.fromEntity(
                foundProductCategory.getProduct(),
                foundProductCategory.getCategory()
        );
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void deleteByProduct(Product product) {
        commandProductCategoryRepository.deleteByProduct(product);
    }


}
