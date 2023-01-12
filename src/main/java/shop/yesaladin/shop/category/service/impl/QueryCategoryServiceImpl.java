package shop.yesaladin.shop.category.service.impl;

import java.util.ArrayList;
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
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 된 CategoryResponse Page 객체
     */
    @Transactional(readOnly = true)
    @Override
    public Page<CategoryResponseDto> findCategoriesByParentId(Pageable pageable, Long parentId) {
        Page<Category> categoryPage = queryCategoryRepository.findCategoriesByParentId(
                pageable,
                parentId
        );

        List<CategoryResponseDto> responseDtos = new ArrayList<>();
        for (Category category : categoryPage) {
            responseDtos.add(CategoryResponseDto.fromEntity(category));
        }

        return new PageImpl<>(responseDtos, pageable, responseDtos.size());
    }

    /**
     *  1차 카테고리를 조회 하기 위한 기능
     *   queryCategoryRepository.findCategories(null,1차 카테고리의 깊이)를 사용하여 조회 (동적 쿼리)
     *
     * @return CategoryResponse 카테고리의 일부 정보를 담고 있는 dto
     */
    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> findParentCategories() {
        return getCategoryResponseDtos(null, Category.DEPTH_PARENT);
    }

    /**
     * parentId 혹은 depth를 통해 category를 찾아 dto로 변환하는 기능
     *
     * @param parentId 찾고자하는 카테고리의 parentId
     * @param depth 찾고자하는 카테고리의 depth
     * @return CategorySimpleDto 카테고리의 기본 정보를 담고있는 dto
     */
    private List<CategoryResponseDto> getCategoryResponseDtos(Long parentId, Integer depth) {
        List<Category> categories = queryCategoryRepository.findCategories(
                parentId,
                depth
        );

        List<CategoryResponseDto> dtoList = new ArrayList<>();
        for (Category category : categories) {
            dtoList.add(CategoryResponseDto.fromEntity(category));
        }
        return dtoList;
    }

    /**
     * 1차 카테고리 id에 해당하는 모든 2차 카테고리를 조회 하는 기능 (paging x)
     *
     * @param parentId 2차 카테고리의 부모 카테고리의 id
     * @return CategoryResponse 카테고리의 일부 정보를 담고 있는 dto
     */
    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> findChildCategoriesByParentId(Long parentId) {
        return getCategoryResponseDtos(parentId, Category.DEPTH_CHILD);
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
    @Transactional(readOnly = true)
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
