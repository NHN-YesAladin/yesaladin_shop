package shop.yesaladin.shop.category.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
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
     *  {@inheritDoc}
     *
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

        return new PageImpl<>(responseDtos, pageable, categoryPage.getTotalElements());
    }

    /**
     * {@inheritDoc}
     */
    @Cacheable(cacheNames = "parentCategories")
    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> findParentCategories() {
        log.info("parentCategories - caching is working soon");
        return getCategoryResponseDtos(null, Category.DEPTH_PARENT);
    }

    /**
     * parentId 혹은 depth를 통해 category를 찾아 dto로 변환하는 기능
     *
     * @param parentId 찾고자하는 카테고리의 parentId
     * @param depth    찾고자하는 카테고리의 depth
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
     * {@inheritDoc}
     */
    @Cacheable(cacheNames = "childCategories", key = "'parentCategory:'+#parentId")
    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> findChildCategoriesByParentId(Long parentId) {

        log.info("childCategories - caching is working soon");
        return getCategoryResponseDtos(parentId, Category.DEPTH_CHILD);
    }


    /**
     *  {@inheritDoc}
     *
     */
    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto findCategoryById(long id) {
        Category category = tryGetCategoryById(id);
        return CategoryResponseDto.fromEntity(category);
    }



    /**
     * 카테고리 조회
     *
     * @param id
     * @return
     * @throws ClientException 해당하는 id의 카테고리가 없을 경우
     */
    private Category tryGetCategoryById(long id) {
        return queryCategoryRepository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with id : " + id
                ));
    }

}
