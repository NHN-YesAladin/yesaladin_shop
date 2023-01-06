package shop.yesaladin.shop.category.controller;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.dto.CategoryUpdateDto;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;

/**
 * Rest Controller 구현
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
}
