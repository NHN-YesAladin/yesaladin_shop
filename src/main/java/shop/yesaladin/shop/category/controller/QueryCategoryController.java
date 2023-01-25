package shop.yesaladin.shop.category.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;

/**
 * 카테고리 조회를 api를 통하여 동작하기 위한 rest controller id, name을 통한 단일 조회 페이징, parent_id를 통한 복수 조회
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/categories")
// 테스트 상황에 카테고리 조회 필요시, Front 프로젝트의 port를 8080 혹은 9090으로 놓고 테스트
@CrossOrigin({"http://localhost:8080", "http://localhost:9090", "http://test.yesaladin.shop:9090",
        "https://www.yesaladin.shop"})
public class QueryCategoryController {

    private final QueryCategoryService queryCategoryService;

    /**
     * 카테고리 단일 조회 기능
     *
     * @param categoryId 조회하고자 하는 카테고리의 id
     * @return 카테고리의 일부 데이터를 반환
     */
    @GetMapping("/{categoryId}")
    public CategoryResponseDto getCategoryById(@PathVariable Long categoryId) {
        return queryCategoryService.findCategoryById(categoryId);
    }

    /**
     * 카테고리 리스트 조회를 paging하여 조회하는 기능
     *
     * @param pageable page 와 size를 자동으로 parsing 하여줌
     * @return 카테고리의 일부 데이터를 페이징데이터와 함게 List 화 하여 전달
     */
    @GetMapping
    public PaginatedResponseDto<CategoryResponseDto> getCategoriesByParentId(
            @RequestParam("parentId") Long parentId,
            Pageable pageable
    ) {
        Page<CategoryResponseDto> data = queryCategoryService.findCategoriesByParentId(
                pageable,
                parentId
        );
        return PaginatedResponseDto.<CategoryResponseDto>builder()
                .currentPage(data.getNumber())
                .totalPage(data.getTotalPages())
                .totalDataCount(data.getTotalElements())
                .dataList(data.getContent())
                .build();
    }

    /**
     * 모든 1차 카테고리에 대해 조회하는 기능
     *
     * @return 카테고리의 일부 데이터를 List화 하여 반환
     */
    @GetMapping(params = "cate=parents")
    public List<CategoryResponseDto> getParentCategories() {
        return queryCategoryService.findParentCategories();
    }

    /**
     * 주어진 1차 카테고리 id를 통해 아래의 자식 카테고리를 모두 조회 하는 기능
     *
     * @param parentId
     * @return
     */
    @GetMapping(value = "/{parentId}", params = "cate=children")
    public List<CategoryResponseDto> getChildCategoriesByParentId(@PathVariable Long parentId) {
        return queryCategoryService.findChildCategoriesByParentId(parentId);
    }

}
