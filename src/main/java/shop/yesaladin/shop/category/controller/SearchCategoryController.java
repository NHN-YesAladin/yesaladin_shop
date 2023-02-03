package shop.yesaladin.shop.category.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryRequestDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.SearchCategoryService;

/**
 * 엘라스틱 서치 카테고리 검색 컨트롤러
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search/categories")
public class SearchCategoryController {
    private final SearchCategoryService searchCategoryService;

    /**
     * 카테고리 이름으로 검색하는 메서드
     *
     * @param dto 카테고리 이름, 페이지 위치, 데이터 갯수
     * @return 검색된 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "name")
    public ResponseDto<SearchCategoryResponseDto> searchCategoryByName(@ModelAttribute @Valid SearchCategoryRequestDto dto) {
        return ResponseDto.<SearchCategoryResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(searchCategoryService.searchCategoryByName(dto))
                .build();
    }
}

