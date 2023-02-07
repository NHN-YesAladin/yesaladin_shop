package shop.yesaladin.shop.category.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.category.dto.CategoryModifyRequestDto;
import shop.yesaladin.shop.category.dto.CategoryRequestDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.ResultCodeDto;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;

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
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:9090",
        "http://test.yesaladin.shop:9090",
        "https://www.yesaladin.shop"})
public class CommandCategoryController {

    private final CommandCategoryService commandCategoryService;


    /**
     * 카테고리를 생성하기위해 Post 요청을 처리하는 기능
     *
     * @param categoryRequest 생성시 필요한 이름, 노출 여부, 상위 카테고리 id가 존재
     * @return ResponseEntity로 카테고리 생성 성공시 생성된 카테고리의 일부 데이터를 반환
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<CategoryResponseDto> createCategory(
            @Valid @RequestBody CategoryRequestDto categoryRequest
    ) {
        CategoryResponseDto categoryResponseDto = commandCategoryService.create(categoryRequest);
        return ResponseDto.<CategoryResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(categoryResponseDto)
                .build();
    }

    /**
     * 카테고리를 수정하기위해 Put 요청을 처리하는 기능
     *
     * @param categoryId         수정하고자 하는 카테고리의 id
     * @param categoryRequestDto 생성시 필요한 이름, 노출 여부, 상위 카테고리 id가 존재
     * @return ResponseEntity로 카테고리 수정 성공시 200 코드 및 카테고리의 일부 데이터 반환
     */
    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<CategoryResponseDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        CategoryResponseDto categoryResponseDto = commandCategoryService.update(
                categoryId,
                categoryRequestDto
        );
        return ResponseDto.<CategoryResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(categoryResponseDto)
                .build();
    }

    /**
     * 카테고리 순서 수정 요청을 처리하는 기능
     *  최악의 경우 모든 카테고리를 수정해야하기 때문에 인자가 List 형식이다.
     *
     * @param requestList 수정을 요청하는 카테고리 리스트
     * @return 결과를 출력해주는 String result를 가지는  객체
     */
    @PutMapping("/order")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<ResultCodeDto> modifyCategoriesOrder(@Valid @RequestBody List<CategoryModifyRequestDto> requestList) {
        commandCategoryService.updateOrder(requestList);
        return ResponseDto.<ResultCodeDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(new ResultCodeDto("Success"))
                .build();
    }

    /**
     * 카테고리를 삭제하기위해 Delete 요청을 처리하는 기능
     *
     * @param categoryId 삭제하고자하는 카테고리의 id
     * @return 결과를 출력해주는 String result를 가지는  객체
     */
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<ResultCodeDto> deleteCategory(@PathVariable Long categoryId) {
        commandCategoryService.delete(categoryId);
        return ResponseDto.<ResultCodeDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(new ResultCodeDto("Success"))
                .build();
    }
}
