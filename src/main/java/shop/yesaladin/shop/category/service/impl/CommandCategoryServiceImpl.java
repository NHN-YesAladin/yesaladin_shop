package shop.yesaladin.shop.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.dto.CategoryUpdateDto;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;

/**
 *  카테고리 CUD용 카테고리 서비스 구현체
 *
 * @author 배수한
 * @since 1.0
 */

@RequiredArgsConstructor
@Service
public class CommandCategoryServiceImpl implements CommandCategoryService {

    private final CommandCategoryRepository commandCategoryRepository;
    private final QueryCategoryService queryCategoryService;
    @Override
    public Category create(CategoryCreateDto createDto) {
        return commandCategoryRepository.save(createDto.toEntity());
    }

    @Override
    public Category update(CategoryUpdateDto updateDto) {
        Category parentCategory = queryCategoryService.findCategoryById(updateDto.getParentId());
        return commandCategoryRepository.save(updateDto.toEntity(parentCategory));
    }
}
