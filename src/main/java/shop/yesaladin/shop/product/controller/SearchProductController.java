package shop.yesaladin.shop.product.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.product.dto.SearchProductRequestDto;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;
import shop.yesaladin.shop.product.service.inter.SearchProductService;

/**
 * 상품 검색 컨트롤러
 *
 * @author : 김선홍
 * @since : 1.0
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/search/product")
public class SearchProductController {

    private final SearchProductService searchProductService;

    /**
     * 상품의 제목으로 검색하는 컨트롤라 메서드
     *
     * @param requestDto 요청한 상품의 제목, 페이징 위치, 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/title")
    List<SearchedProductResponseDto> searchProductByTitle(@Valid @RequestBody SearchProductRequestDto requestDto) {
        return searchProductService.searchProductsByProductTitle(
                requestDto.getQuery(), requestDto.getOffset(), requestDto.getSize());
    }

    /**
     * 상품의 내용으로 검색하는 컨트롤러 메서드
     *
     * @param requestDto 요청한 상품의 내용, 페이징 위치, 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/content")
    List<SearchedProductResponseDto> searchProductByContent(@Valid @RequestBody SearchProductRequestDto requestDto) {
        return searchProductService.searchProductsByProductContent(
                requestDto.getQuery(), requestDto.getOffset(), requestDto.getSize());
    }

    /**
     * 상품의 ISBN으로 검색하는 컨트롤러 메서드
     *
     * @param requestDto 요청한 상품의 ISBN, 페이징 위치, 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/isbn")
    List<SearchedProductResponseDto> searchProductByISBN(@Valid @RequestBody SearchProductRequestDto requestDto) {
        return searchProductService.searchProductsByProductISBN(
                requestDto.getQuery(), requestDto.getOffset(), requestDto.getSize());
    }

    /**
     * 상품의 작가 이름으로 검색하는 컨트롤러 메서드
     *
     * @param requestDto 요청한 상품의 작가 이름, 페이징 위치, 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/author")
    List<SearchedProductResponseDto> searchProductByAuthor(@Valid @RequestBody SearchProductRequestDto requestDto) {
        return searchProductService.searchProductsByProductAuthor(
                requestDto.getQuery(), requestDto.getOffset(), requestDto.getSize());
    }

    /**
     * 상품의 출판사로 검색하는 컨트롤러 메서드
     *
     * @param requestDto 요청한 상품의 출판사 이름, 페이징 위치, 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/publisher")
    List<SearchedProductResponseDto> searchProductByPublisher(@Valid @RequestBody SearchProductRequestDto requestDto) {
        return searchProductService.searchProductsByPublisher(
                requestDto.getQuery(), requestDto.getOffset(), requestDto.getSize());
    }

    /**
     * 상품의 태그로 검색하는 컨트롤러 메서드
     *
     * @param requestDto 요청한 상품의 태그 이름, 페이징 위치, 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/tag")
    List<SearchedProductResponseDto> searchProductByTag(@Valid @RequestBody SearchProductRequestDto requestDto) {
        return searchProductService.searchProductsByTag(
                requestDto.getQuery(), requestDto.getOffset(), requestDto.getSize());
    }

    /**
     * 카테고리 id로 상품을 검색하는 컨트롤러 메서드
     *
     * @param requestDto 요청한 상품의 카테고리 id, 페이징 위치, 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/category/id/{id}")
    List<SearchedProductResponseDto> searchProductByCategoryId(
            @PathVariable Long id,
            @RequestParam(name = "offset") @Min(value = 0) Integer offset,
            @RequestParam(name = "size") @Min(value = 1) @Max(value = 20) Integer size
    ) {
        return searchProductService.searchProductsByCategoryId(id, offset, size);
    }

    /**
     * 카테고리 이름으로 상품을 검색하는 컨트롤러 메서드
     *
     * @param requestDto 요청한 상품의 카테고리 이름, 페이징 위치, 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/category/name")
    List<SearchedProductResponseDto> searchProductByCategoryName(@Valid @RequestBody SearchProductRequestDto requestDto) {
        return searchProductService.searchProductsByCategoryName(
                requestDto.getQuery(),
                requestDto.getOffset(),
                requestDto.getSize()
        );
    }
}
