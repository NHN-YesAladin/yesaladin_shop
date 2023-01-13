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
import shop.yesaladin.shop.category.dto.CategoriesSimpleResponseDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.CategorySimpleDto;
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
     * 메인 페이지에서 사용하는 메서드로, 모든 카테고리를 불러올때 사용.
     *   1. findSimpleDtosByDepth(부모 깊이)를 통해 1차 카테고리에 대한 기본 정보를 read
     *   2. for-each를 통해 CategoriesSimpleResponseDto 리스트에 add
     *     2-1. CategorySimpleDto(아이디,이름,순서,노출여부) 를 생성
     *     2-2. findSimpleDtosByParentId(부모 아이디)를 통해 2차 카테고리에 대한 기본 정보를 read
     *     2-3. CategoriesSimpleResponseDto를 생성
     *
     * @see  CategoriesSimpleResponseDto
     *         : 1차 카테고리의 CategorySimpleDto, 2차 카테고리의 CategorySimpleDto 리스트
     *
     * @return 카테고리의 기본 정보를 담은 CategoriesSimpleResponseDto의 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public List<CategoriesSimpleResponseDto> findAllCategoryResponseDto() {
        //TODO 전체 카테고리 조회 성능 개선 필요
        List<CategoriesSimpleResponseDto> dtoList = Collections.synchronizedList(new ArrayList<>());

        List<CategorySimpleDto> parentSimpleDtos = queryCategoryRepository.findSimpleDtosByDepth(
                Category.DEPTH_PARENT);
        for (CategorySimpleDto parentSimpleDto : parentSimpleDtos) {
            dtoList.add(new CategoriesSimpleResponseDto(new CategorySimpleDto(
                    parentSimpleDto.getId(),
                    parentSimpleDto.getName(),
                    parentSimpleDto.getIsShown(),
                    parentSimpleDto.getOrder()
            ), queryCategoryRepository.findSimpleDtosByParentId(parentSimpleDto.getId())));
        }
        return dtoList;
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
        return queryCategoryRepository.findByIdByFetching(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

}
