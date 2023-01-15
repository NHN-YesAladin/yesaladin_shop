package shop.yesaladin.shop.category.controller;

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
import shop.yesaladin.shop.category.dto.CategoryRequestDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.ResultCodeDto;
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


    /**
     * 카테고리를 생성하기위해 Post 요청을 처리하는 기능
     *
     * @param categoryRequestDto 생성시 필요한 이름, 노출 여부, 상위 카테고리 id가 존재
     * @return ResponseEntity로 카테고리 생성 성공시 생성된 카테고리의 일부 데이터를 반환
     */
    @PostMapping
    public CategoryResponseDto createCategory(
            @Valid @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        CategoryResponseDto categoryResponseDto = commandCategoryService.create(categoryRequestDto);
        return categoryResponseDto;
    }

    /**
     * 카테고리를 수정하기위해 Put 요청을 처리하는 기능
     *
     * @param categoryId         수정하고자 하는 카테고리의 id
     * @param categoryRequestDto 생성시 필요한 이름, 노출 여부, 상위 카테고리 id가 존재
     * @return ResponseEntity로 카테고리 수정 성공시 200 코드 및 카테고리의 일부 데이터 반환
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        CategoryResponseDto categoryResponseDto = commandCategoryService.update(
                categoryId,
                categoryRequestDto
        );
        return ResponseEntity.ok(categoryResponseDto);
    }

    /**
     * 카테고리를 삭제하기위해 Delete 요청을 처리하는 기능
     *
     * @param categoryId 삭제하고자하는 카테고리의 id
     * @return 결과를 출력해주는 String result를 가지는  객체
     */
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultCodeDto deleteCategory(@PathVariable Long categoryId) {
        commandCategoryService.delete(categoryId);
        return new ResultCodeDto("Success");
    }
}
