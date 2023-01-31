package shop.yesaladin.shop.writing.controller;


import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.writing.dto.SearchAuthorRequestDto;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto;
import shop.yesaladin.shop.writing.service.inter.SearchAuthorService;

/**
 * 엘라스틱서치 저자 검색 컨트롤러
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search/authors")
public class SearchAuthorController {

    private final SearchAuthorService service;

    /**
     * 저자의 이름으로 검색하는 메서드
     *
     * @param searchAuthorRequestDto 저자의 이름, 페이지 위치, 데이터 갯수
     * @return 저자 리스트 및 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "name")
    ResponseDto<SearchedAuthorResponseDto> searchAuthorByName(@ModelAttribute @Valid SearchAuthorRequestDto searchAuthorRequestDto) {
        return ResponseDto.<SearchedAuthorResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(service.searchAuthorByName(searchAuthorRequestDto))
                .build();
    }

}
