package shop.yesaladin.shop.category.controller;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.exception.ValidationFailedException;
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
            @Valid @RequestBody CategoryCreateDto createDto,
            BindingResult bindingResult
    ) throws URISyntaxException {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        Category category = commandCategoryService.create(createDto);
        return ResponseEntity.created(new URI(category.getId().toString())).build();
    }

}
