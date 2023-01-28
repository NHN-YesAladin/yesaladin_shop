package shop.yesaladin.shop.product.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/search/products")
public class SearchProductController {

    private final SearchProductService searchProductService;

    /**
     * 상품의 제목으로 검색하는 컨트롤라 메서드
     *
     * @param title 검색할 상품의 title
     * @param offset 검색할 페이지의 위치
     * @param size 데이터 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "title")
    public List<SearchedProductResponseDto> searchProductByTitle(
            @RequestParam String title,
            @RequestParam(name = "offset") @Min(value = 0) Integer offset,
            @RequestParam(name = "size") @Min(value = 1) @Max(value = 20) Integer size

    ) {
        return searchProductService.searchProductsByProductTitle(title, offset, size);
    }

    /**
     * 상품의 내용으로 검색하는 컨트롤러 메서드
     *
     * @param content 검색할 상품의 내용
     * @param offset 검색할 페이지의 위치
     * @param size 데이터 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "content")
    public List<SearchedProductResponseDto> searchProductByContent(
            @RequestParam String content,
            @RequestParam(name = "offset") @Min(value = 0) Integer offset,
            @RequestParam(name = "size") @Min(value = 1) @Max(value = 20) Integer size
    ) {
        return searchProductService.searchProductsByProductContent(content, offset, size);
    }

    /**
     * 상품의 ISBN으로 검색하는 컨트롤러 메서드
     *
     * @param isbn 검색할 상품의 isbn
     * @param offset 검색할 페이지의 위치
     * @param size 데이터 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "isbn")
    public List<SearchedProductResponseDto> searchProductByISBN(
            @RequestParam String isbn,
            @RequestParam(name = "offset") @Min(value = 0) Integer offset,
            @RequestParam(name = "size") @Min(value = 1) @Max(value = 20) Integer size
    ) {
        return searchProductService.searchProductsByProductISBN(
                isbn, offset, size);
    }

    /**
     * 상품의 작가 이름으로 검색하는 컨트롤러 메서드
     *
     * @param author 검색할 상품의 저자명
     * @param offset 검색할 페이지의 위치
     * @param size 데이터 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "author")
    public List<SearchedProductResponseDto> searchProductByAuthor(
            @RequestParam String author,
            @RequestParam(name = "offset") @Min(value = 0) Integer offset,
            @RequestParam(name = "size") @Min(value = 1) @Max(value = 20) Integer size) {
        return searchProductService.searchProductsByProductAuthor(author, offset, size);
    }

    /**
     * 상품의 출판사로 검색하는 컨트롤러 메서드
     *
     * @param publisher 검색할 상품의 출판사
     * @param offset 검색할 페이지의 위치
     * @param size 데이터 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping( params = "publisher")
    public List<SearchedProductResponseDto> searchProductByPublisher(
            @RequestParam String publisher,
            @RequestParam(name = "offset") @Min(value = 0) Integer offset,
            @RequestParam(name = "size") @Min(value = 1) @Max(value = 20) Integer size) {
        return searchProductService.searchProductsByPublisher(publisher, offset, size);
    }

    /**
     * 상품의 태그로 검색하는 컨트롤러 메서드
     *
     * @param tag 검색할 상품의 태그
     * @param offset 검색할 페이지의 위치
     * @param size 데이터 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "tag")
    public List<SearchedProductResponseDto> searchProductByTag(
            @RequestParam String tag,
            @RequestParam(name = "offset") @Min(value = 0) Integer offset,
            @RequestParam(name = "size") @Min(value = 1) @Max(value = 20) Integer size) {
        return searchProductService.searchProductsByTag(tag, offset, size);
    }

    /**
     * 카테고리 id로 상품을 검색하는 컨트롤러 메서드
     *
     * @param id 검색할 카테고리의 id
     * @param offset 검색할 페이지의 위치
     * @param size 데이터 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "categoryid")
    public List<SearchedProductResponseDto> searchProductByCategoryId(
            @RequestParam(name = "categoryid") Long id,
            @RequestParam(name = "offset") @Min(value = 0) Integer offset,
            @RequestParam(name = "size") @Min(value = 1) @Max(value = 20) Integer size
    ) {
        return searchProductService.searchProductsByCategoryId(id, offset, size);
    }

    /**
     * 카테고리 이름으로 상품을 검색하는 컨트롤러 메서드
     *
     * @param name 검색할 카테고리의 id
     * @param offset 검색할 페이지의 위치
     * @param size 데이터 갯수
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "categoryname")
    public List<SearchedProductResponseDto> searchProductByCategoryName(
            @RequestParam(name = "categoryname") String name,
            @RequestParam(name = "offset") @Min(value = 0) Integer offset,
            @RequestParam(name = "size") @Min(value = 1) @Max(value = 20) Integer size
    ) {
        return searchProductService.searchProductsByCategoryName(name, offset, size);
    }
}
