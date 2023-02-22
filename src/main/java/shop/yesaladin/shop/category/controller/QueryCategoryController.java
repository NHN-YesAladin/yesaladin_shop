package shop.yesaladin.shop.category.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
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
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:9090",
        "http://test.yesaladin.shop:9090",
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
    public ResponseDto<CategoryResponseDto> getCategoryById(@PathVariable Long categoryId) {
        CategoryResponseDto category = queryCategoryService.findCategoryById(categoryId);

        return ResponseDto.<CategoryResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(category)
                .build();
    }

    /**
     * 카테고리 리스트 조회를 paging하여 조회하는 기능
     *
     * @param pageable page 와 size를 자동으로 parsing 하여줌
     * @return 카테고리의 일부 데이터를 페이징데이터와 함게 List 화 하여 전달
     */
    @GetMapping
    public ResponseDto<PaginatedResponseDto<CategoryResponseDto>> getCategoriesByParentId(
            @RequestParam("parentId") Long parentId,
            Pageable pageable
    ) {
        Page<CategoryResponseDto> data = queryCategoryService.findCategoriesByParentId(
                pageable,
                parentId
        );
        PaginatedResponseDto<CategoryResponseDto> paginatedResponseDto = PaginatedResponseDto.<CategoryResponseDto>builder()
                .currentPage(data.getNumber())
                .totalPage(data.getTotalPages())
                .totalDataCount(data.getTotalElements())
                .dataList(data.getContent())
                .build();
        return ResponseDto.<PaginatedResponseDto<CategoryResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(paginatedResponseDto)
                .build();
    }

    /**
     * 모든 1차 카테고리에 대해 조회하는 기능
     *
     * @return 카테고리의 일부 데이터를 List화 하여 반환
     */
    @GetMapping(params = "cate=parents")
    public ResponseDto<List<CategoryResponseDto>> getParentCategories() {
        List<CategoryResponseDto> parentCategories = queryCategoryService.findParentCategories();

        return ResponseDto.<List<CategoryResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(parentCategories)
                .build();
    }

    /**
     * 주어진 1차 카테고리 id를 통해 아래의 자식 카테고리를 모두 조회 하는 기능
     *
     * @param parentId
     * @return 카테고리의 일부 데이터를 List화 하여 반환
     */
    @GetMapping(value = "/{parentId}", params = "cate=children")
    public ResponseDto<List<CategoryResponseDto>> getChildCategoriesByParentId(@PathVariable Long parentId) {
        List<CategoryResponseDto> childCategoriesByParentId = queryCategoryService.findChildCategoriesByParentId(
                parentId);
        return ResponseDto.<List<CategoryResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(childCategoriesByParentId)
                .build();
    }

}
