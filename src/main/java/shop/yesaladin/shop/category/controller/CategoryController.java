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
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.dto.CategoryDeleteDto;
import shop.yesaladin.shop.category.dto.CategoryUpdateDto;
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
public class CategoryController {

    private final CommandCategoryService commandCategoryService;

    @PostMapping
    public ResponseEntity createCategory(
            @Valid @RequestBody CategoryCreateDto createDto
    ) throws URISyntaxException {
        Category category = commandCategoryService.create(createDto);
        return ResponseEntity.created(new URI(category.getId().toString())).build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateDto updateDto
    ) {
        updateDto.setId(categoryId);
        commandCategoryService.update(updateDto);
        return ResponseEntity.ok(updateDto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultCode deleteCategory(@PathVariable Long categoryId) {
        commandCategoryService.delete(new CategoryDeleteDto(categoryId));
        return new ResultCode("Success");
    }
}
