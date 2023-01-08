package shop.yesaladin.shop.category.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponse;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;

/**
 * 카테고리 조회용 서비스 구현체
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class QueryCategoryServiceImpl implements QueryCategoryService {

    private final QueryCategoryRepository queryCategoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<CategoryResponse> findCategories(Pageable pageable) {
        Page<Category> categoryPage = queryCategoryRepository.findAll(pageable);

        List<CategoryResponse> responseDtos = Collections.synchronizedList(new ArrayList<>());
        for (Category category : categoryPage) {
            responseDtos.add(CategoryResponse.fromEntity(category));
        }

        return new PageImpl<>(responseDtos, pageable, responseDtos.size());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse findCategoryById(long id) {
        Category category = queryCategoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return CategoryResponse.fromEntity(category);
    }

    //TODO 테스트 필요
    @Override
    public Category findParentCategoryById(long id) {
        return queryCategoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

}
