package shop.yesaladin.shop.category.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;

/**
 * 카테고리 조회를 api를 통하여 동작하기 위한 rest controller
 *   id, name을 통한 단일 조회
 *   페이징, parent_id를 통한 복수 조회
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/categories")
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
     * @return 카테고리의 일부 데이터를 List 화 하여 전달
     */
    @GetMapping
    public List<CategoryResponseDto> getCategories(Pageable pageable) {
        return queryCategoryService.findCategories(pageable).getContent();
    }
}
