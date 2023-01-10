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
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
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

    /**
     * 페이징된 카테고리 리스트 조회를 위한 기능
     *  동시성 이슈를 해결하기위해 Collections.synchronizedList를 사용
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 된 CategoryResponse Page 객체
     */
    @Transactional(readOnly = true)
    @Override
    public Page<CategoryResponseDto> findCategories(Pageable pageable) {
        Page<Category> categoryPage = queryCategoryRepository.findAll(pageable);

        List<CategoryResponseDto> responseDtos = Collections.synchronizedList(new ArrayList<>());
        for (Category category : categoryPage) {
            responseDtos.add(CategoryResponseDto.fromEntity(category));
        }

        return new PageImpl<>(responseDtos, pageable, responseDtos.size());
    }

    /**
     * 단일 카테고리 조회를 위한 기능
     *
     * @param id 조회하고자 하는 카테고리 id
     * @return CategoryResponse 카테고리의 일부 정보를 담고 있는 dto
     */
    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto findCategoryById(long id) {
        Category category = tryGetCategoryById(id);
        return CategoryResponseDto.fromEntity(category);
    }

    /**
     * 카테고리 id를 통해 부모 카테고리를 조회 하기위한 기능
     *
     * @param id 부모 카테고리의 id
     * @return 조회된 부모 Category
     */
    //TODO 테스트 필요 - 카테고리 자기 참조 구현시 테스트 예정
    @Override
    public Category findInnerCategoryById(long id) {
        return tryGetCategoryById(id);
    }

    /**
     * 카테고리 조회
     * @throws CategoryNotFoundException 해당하는 id의 카테고리가 없을 경우
     *
     * @param id
     * @return
     */
    private Category tryGetCategoryById(long id) {
        return queryCategoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

}
