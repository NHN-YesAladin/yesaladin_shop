package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.product.dto.SearchProductPageRequestDto;
import shop.yesaladin.shop.product.dto.SearchedProductManagerResponseDto;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;
import shop.yesaladin.shop.product.service.inter.SearchProductService;

import javax.validation.Valid;

/**
 * 상품 검색 컨트롤러
 *
 * @author : 김선홍
 * @since : 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search/products")
public class SearchProductController {

    private final SearchProductService searchProductService;

    /**
     * 상품의 제목으로 검색하는 컨트롤라 메서드
     *
     * @param title       검색할 상품의 title
     * @param pageRequest 페이징
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "title")
    public ResponseDto<SearchedProductResponseDto> searchProductByTitle(
            @RequestParam String title,
            @ModelAttribute @Valid SearchProductPageRequestDto pageRequest
    ) {
        return ResponseDto.<SearchedProductResponseDto>builder()
                .success(true)
                .data(searchProductService.searchProductsByProductTitle(
                        title,
                        pageRequest.getOffset(),
                        pageRequest.getSize()
                ))
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 내용으로 검색하는 컨트롤러 메서드
     *
     * @param content     검색할 상품의 내용
     * @param pageRequest 페이징
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "content")
    public ResponseDto<SearchedProductResponseDto> searchProductByContent(
            @RequestParam String content,
            @ModelAttribute @Valid SearchProductPageRequestDto pageRequest
    ) {
        return ResponseDto.<SearchedProductResponseDto>builder()
                .success(true)
                .data(searchProductService.searchProductsByProductContent(
                        content,
                        pageRequest.getOffset(),
                        pageRequest.getSize()
                ))
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 ISBN으로 검색하는 컨트롤러 메서드
     *
     * @param isbn        검색할 상품의 isbn
     * @param pageRequest 페이징
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "isbn")
    public ResponseDto<SearchedProductResponseDto> searchProductByISBN(
            @RequestParam String isbn,
            @ModelAttribute @Valid SearchProductPageRequestDto pageRequest
    ) {
        return ResponseDto.<SearchedProductResponseDto>builder()
                .success(true)
                .data(searchProductService.searchProductsByProductISBN(
                        isbn,
                        pageRequest.getOffset(),
                        pageRequest.getSize()
                ))
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 작가 이름으로 검색하는 컨트롤러 메서드
     *
     * @param author      검색할 상품의 저자명
     * @param pageRequest 페이징
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "author")
    public ResponseDto<SearchedProductResponseDto> searchProductByAuthor(
            @RequestParam String author,
            @ModelAttribute @Valid SearchProductPageRequestDto pageRequest
    ) {
        return ResponseDto.<SearchedProductResponseDto>builder()
                .success(true)
                .data(searchProductService.searchProductsByProductAuthor(
                        author,
                        pageRequest.getOffset(),
                        pageRequest.getSize()
                ))
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 출판사로 검색하는 컨트롤러 메서드
     *
     * @param publisher   검색할 상품의 출판사
     * @param pageRequest 페이징
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "publisher")
    public ResponseDto<SearchedProductResponseDto> searchProductByPublisher(
            @RequestParam String publisher,
            @ModelAttribute @Valid SearchProductPageRequestDto pageRequest
    ) {
        return ResponseDto.<SearchedProductResponseDto>builder()
                .success(true)
                .data(searchProductService.searchProductsByPublisher(
                        publisher,
                        pageRequest.getOffset(),
                        pageRequest.getSize()
                ))
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 태그로 검색하는 컨트롤러 메서드
     *
     * @param tag         검색할 상품의 태그
     * @param pageRequest 페이징
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "tag")
    public ResponseDto<SearchedProductResponseDto> searchProductByTag(
            @RequestParam String tag,
            @ModelAttribute @Valid SearchProductPageRequestDto pageRequest
    ) {
        return ResponseDto.<SearchedProductResponseDto>builder()
                .success(true)
                .data(searchProductService.searchProductsByTag(
                        tag,
                        pageRequest.getOffset(),
                        pageRequest.getSize()
                ))
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 카테고리 id로 상품을 검색하는 컨트롤러 메서드
     *
     * @param id          검색할 카테고리의 id
     * @param pageRequest 페이징
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "categoryid")
    public ResponseDto<SearchedProductManagerResponseDto> searchProductByCategoryId(
            @RequestParam(name = "categoryid") Long id,
            @ModelAttribute @Valid SearchProductPageRequestDto pageRequest
    ) {
        return ResponseDto.<SearchedProductManagerResponseDto>builder()
                .success(true)
                .data(searchProductService.searchProductsByCategoryId(
                        id,
                        pageRequest.getOffset(),
                        pageRequest.getSize()
                ))
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 카테고리 이름으로 상품을 검색하는 컨트롤러 메서드
     *
     * @param name        검색할 카테고리의 id
     * @param pageRequest 페이징
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "categoryname")
    public ResponseDto<SearchedProductManagerResponseDto> searchProductByCategoryName(
            @RequestParam(name = "categoryname") String name,
            @ModelAttribute @Valid SearchProductPageRequestDto pageRequest
    ) {
        return ResponseDto.<SearchedProductManagerResponseDto>builder()
                .success(true)
                .data(searchProductService.searchProductsByCategoryName(
                        name,
                        pageRequest.getOffset(),
                        pageRequest.getSize()
                ))
                .status(HttpStatus.OK)
                .build();
    }
}
