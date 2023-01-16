package shop.yesaladin.shop.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;
import shop.yesaladin.shop.category.domain.repository.CommandProductCategoryRepository;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.ProductCategoryIdDto;
import shop.yesaladin.shop.category.service.inter.CommandProductCategoryService;

/**
 * @author 배수한
 * @since 1.0
 */

@RequiredArgsConstructor
@Service
public class CommandProductCategoryServiceImpl implements CommandProductCategoryService {

    private final CommandProductCategoryRepository commandProductCategoryRepository;
    private final QueryCategoryRepository queryCategoryRepository;

    @Override
    public ProductCategoryIdDto register(ProductCategoryIdDto idDto) {
//        queryCategoryRepository.findSimpleDtosByDepth()
//        commandProductCategoryRepository.save()
        return null;
    }

    @Override
    public void delete(Pk pk) {

    }
}
