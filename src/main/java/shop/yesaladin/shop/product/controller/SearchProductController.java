package shop.yesaladin.shop.product.controller;

import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
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
@RequestMapping("/v1/search/products")
public class SearchProductController {

    private final SearchProductService searchProductService;

    /**
     * 상품의 제목으로 검색하는 컨트롤라 메서드
     *
     * @param title    검색할 상품의 title
     * @param pageable 페이지정보
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "title")
    public ResponseDto<PaginatedResponseDto<SearchedProductResponseDto>> searchProductByTitle(
            @RequestParam @Size(max = 30) String title,
            @PageableDefault Pageable pageable
    ) {
        Page<SearchedProductResponseDto> response = searchProductService.searchProductsByProductTitle(
                title,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<SearchedProductResponseDto>>builder()
                .success(true)
                .data(PaginatedResponseDto.<SearchedProductResponseDto>builder()
                        .dataList(response.toList())
                        .totalDataCount(response.getTotalElements())
                        .currentPage(response.getNumber())
                        .totalPage(response.getTotalPages())
                        .build())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 내용으로 검색하는 컨트롤러 메서드
     *
     * @param content  검색할 상품의 내용
     * @param pageable 페이지정보
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "content")
    public ResponseDto<PaginatedResponseDto<SearchedProductResponseDto>> searchProductByContent(
            @RequestParam @Size(max = 30) String content,
            @PageableDefault Pageable pageable
    ) {
        Page<SearchedProductResponseDto> response = searchProductService.searchProductsByProductContent(
                content,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<SearchedProductResponseDto>>builder()
                .success(true)
                .data(PaginatedResponseDto.<SearchedProductResponseDto>builder()
                        .dataList(response.toList())
                        .totalDataCount(response.getTotalElements())
                        .currentPage(response.getNumber())
                        .totalPage(response.getTotalPages())
                        .build())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 ISBN으로 검색하는 컨트롤러 메서드
     *
     * @param isbn     검색할 상품의 isbn
     * @param pageable 페이지정보
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "isbn")
    public ResponseDto<PaginatedResponseDto<SearchedProductResponseDto>> searchProductByISBN(
            @RequestParam @Size(max = 30) String isbn,
            @PageableDefault Pageable pageable
    ) {
        Page<SearchedProductResponseDto> response = searchProductService.searchProductsByProductISBN(
                isbn,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<SearchedProductResponseDto>>builder()
                .success(true)
                .data(PaginatedResponseDto.<SearchedProductResponseDto>builder()
                        .dataList(response.toList())
                        .totalDataCount(response.getTotalElements())
                        .currentPage(response.getNumber())
                        .totalPage(response.getTotalPages())
                        .build())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 작가 이름으로 검색하는 컨트롤러 메서드
     *
     * @param author   검색할 상품의 저자명
     * @param pageable 페이지정보
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "author")
    public ResponseDto<PaginatedResponseDto<SearchedProductResponseDto>> searchProductByAuthor(
            @RequestParam @Size(max = 30) String author,
            @PageableDefault Pageable pageable
    ) {
        Page<SearchedProductResponseDto> response = searchProductService.searchProductsByProductAuthor(
                author,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<SearchedProductResponseDto>>builder()
                .success(true)
                .data(PaginatedResponseDto.<SearchedProductResponseDto>builder()
                        .dataList(response.toList())
                        .totalDataCount(response.getTotalElements())
                        .currentPage(response.getNumber())
                        .totalPage(response.getTotalPages())
                        .build())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 출판사로 검색하는 컨트롤러 메서드
     *
     * @param publisher 검색할 상품의 출판사
     * @param pageable  페이지정보
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "publisher")
    public ResponseDto<PaginatedResponseDto<SearchedProductResponseDto>> searchProductByPublisher(
            @RequestParam @Size(max = 30) String publisher,
            @PageableDefault Pageable pageable
    ) {
        Page<SearchedProductResponseDto> response = searchProductService.searchProductsByPublisher(
                publisher,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<SearchedProductResponseDto>>builder()
                .success(true)
                .data(PaginatedResponseDto.<SearchedProductResponseDto>builder()
                        .dataList(response.toList())
                        .totalDataCount(response.getTotalElements())
                        .currentPage(response.getNumber())
                        .totalPage(response.getTotalPages())
                        .build())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 상품의 태그로 검색하는 컨트롤러 메서드
     *
     * @param tag      검색할 상품의 태그
     * @param pageable 페이지정보
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "tag")
    public ResponseDto<PaginatedResponseDto<SearchedProductResponseDto>> searchProductByTag(
            @RequestParam @Size(max = 30) String tag,
            @PageableDefault Pageable pageable
    ) {
        Page<SearchedProductResponseDto> response = searchProductService.searchProductsByTag(
                tag,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<SearchedProductResponseDto>>builder()
                .success(true)
                .data(PaginatedResponseDto.<SearchedProductResponseDto>builder()
                        .dataList(response.toList())
                        .totalDataCount(response.getTotalElements())
                        .currentPage(response.getNumber())
                        .totalPage(response.getTotalPages())
                        .build())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 카테고리 id로 상품을 검색하는 컨트롤러 메서드
     *
     * @param id       검색할 카테고리의 id
     * @param pageable 페이지정보
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "categoryid")
    public ResponseDto<PaginatedResponseDto<SearchedProductResponseDto>> searchProductByCategoryId(
            @RequestParam(name = "categoryid") Long id,
            @PageableDefault Pageable pageable
    ) {
        Page<SearchedProductResponseDto> response = searchProductService.searchProductsByCategoryId(
                id,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<SearchedProductResponseDto>>builder()
                .success(true)
                .data(PaginatedResponseDto.<SearchedProductResponseDto>builder()
                        .dataList(response.toList())
                        .totalDataCount(response.getTotalElements())
                        .currentPage(response.getNumber())
                        .totalPage(response.getTotalPages())
                        .build())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * 카테고리 이름으로 상품을 검색하는 컨트롤러 메서드
     *
     * @param name     검색할 카테고리의 id
     * @param pageable 페이지정보
     * @return 요청된 조건에 대한 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "categoryname")
    public ResponseDto<PaginatedResponseDto<SearchedProductResponseDto>> searchProductByCategoryName(
            @RequestParam(name = "categoryname") String name,
            @PageableDefault Pageable pageable
    ) {
        Page<SearchedProductResponseDto> response = searchProductService.searchProductsByCategoryName(
                name,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<SearchedProductResponseDto>>builder()
                .success(true)
                .data(PaginatedResponseDto.<SearchedProductResponseDto>builder()
                        .dataList(response.toList())
                        .totalDataCount(response.getTotalElements())
                        .currentPage(response.getNumber())
                        .totalPage(response.getTotalPages())
                        .build())
                .status(HttpStatus.OK)
                .build();
    }
}
