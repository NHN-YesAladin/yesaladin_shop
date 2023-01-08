package shop.yesaladin.shop.category.controller;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.category.dto.CategoryRequest;
import shop.yesaladin.shop.category.dto.CategoryOnlyId;
import shop.yesaladin.shop.category.dto.CategoryResponse;
import shop.yesaladin.shop.category.dto.ResultCode;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;

/**
 * 카테고리 생성,수정,삭제를 api를 통하여 동작하기 위한 rest controller
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/categories")
public class CommandCategoryController {

    private final CommandCategoryService commandCategoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest
    ) throws URISyntaxException {
        CategoryResponse categoryResponse = commandCategoryService.create(categoryRequest);
        return ResponseEntity.created(new URI(categoryResponse.getId().toString())).build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) {
        CategoryResponse categoryResponse = commandCategoryService.update(
                categoryId,
                categoryRequest
        );
        return ResponseEntity.ok(categoryResponse);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultCode deleteCategory(@PathVariable Long categoryId) {
        commandCategoryService.delete(new CategoryOnlyId(categoryId));
        return new ResultCode("Success");
    }
}
