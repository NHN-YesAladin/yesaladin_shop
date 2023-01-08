package shop.yesaladin.shop.category.service.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryRequest;
import shop.yesaladin.shop.category.dto.CategoryDeleteDto;
import shop.yesaladin.shop.category.dto.CategoryResponse;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;

/**
 * 카테고리 CUD용 카테고리 서비스 구현체
 *
 * @author 배수한
 * @since 1.0
 */

@RequiredArgsConstructor
@Service
public class CommandCategoryServiceImpl implements CommandCategoryService {

    private final CommandCategoryRepository commandCategoryRepository;
    private final QueryCategoryService queryCategoryService;

    @Transactional
    @Override
    public CategoryResponse create(CategoryRequest createRequest) {
        Category parentCategory = null;
        if (Objects.nonNull(createRequest.getParentId())) {
            parentCategory = queryCategoryService.findParentCategoryById(createRequest.getParentId());
        }
        Category category = commandCategoryRepository.save(createRequest.toEntity(parentCategory));
        return CategoryResponse.fromEntity(category);
    }


    @Transactional
    @Override
    public CategoryResponse update(Long id, CategoryRequest createRequest) {
        Category parentCategory = null;
        if (Objects.nonNull(createRequest.getParentId())) {
            parentCategory = queryCategoryService.findParentCategoryById(createRequest.getParentId());
        }
        Category category = commandCategoryRepository.save(createRequest.toEntity(
                id,
                parentCategory
        ));
        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    @Override
    public void delete(CategoryDeleteDto deleteDto) {
        commandCategoryRepository.deleteById(deleteDto.getId());
    }
}
