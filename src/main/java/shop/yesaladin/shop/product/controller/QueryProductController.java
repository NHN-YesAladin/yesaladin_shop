package shop.yesaladin.shop.product.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.product.dto.ProductModifyDto;
import shop.yesaladin.shop.product.dto.ProductOnlyTitleDto;
import shop.yesaladin.shop.product.dto.ProductRecentResponseDto;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
import shop.yesaladin.shop.product.dto.RecentViewProductRequestDto;
import shop.yesaladin.shop.product.dto.RelationsResponseDto;
import shop.yesaladin.shop.product.dto.ViewCartDto;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

/**
 * 상품 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop",
        "https://test.yesaladin.shop"})
@RestController
@RequestMapping("/v1/products")
public class QueryProductController {

    private final QueryProductService queryProductService;

    /**
     * [GET /products/info/{isbn}] 요청을 받아 상품의 제목을 반환합니다.
     *
     * @param isbn 조회하고자 하는 상품의 Isbn
     * @return 조회한 상품의 제목을 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/info/{isbn}")
    public ResponseDto<ProductOnlyTitleDto> findTitleByIsbn(@PathVariable String isbn) {
        return ResponseDto.<ProductOnlyTitleDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findTitleByIsbn(isbn))
                .build();
    }

    /**
     * [GET /products/check/{isbn}] 요청을 받아 ISBN 을 중복체크합니다.
     *
     * @param isbn 조회하고자 하는 Isbn
     * @return 조회한 여부를 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/check/{isbn}")
    public ResponseDto<Boolean> existsByIsbn(@PathVariable String isbn) {
        return ResponseDto.<Boolean>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.existsByIsbn(isbn))
                .build();
    }

    /**
     * [GET /products/quantity/{id}] 요청을 받아 상품의 수량을 반환합니다.
     *
     * @param id 조회하고자 하는 상품의 Id
     * @return 조회한 상품의 수량을 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/quantity/{id}")
    public ResponseDto<Long> findQuantityById(@PathVariable Long id) {
        return ResponseDto.<Long>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findQuantityById(id))
                .build();
    }

    /**
     * [GET /products/cart] 요청을 받아 장바구니에 나타낼 상품의 정보를 조회합니다.
     *
     * @param cart 찾고자하는 Cart의 정보를 담은 Map
     * @return 정보조회한 List를 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/cart")
    public ResponseDto<List<ViewCartDto>> getCartProducts(
            @RequestParam Map<String, String> cart
    ) {
        return ResponseDto.<List<ViewCartDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.getCartProduct(cart))
                .build();
    }

    /**
     * [GET /products/{id}] 요청을 받아 상품을 상세 조회합니다.
     *
     * @param id 조회하고자 하는 상품의 Id
     * @return 조회한 상품의 상세를 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/{id}")
    public ResponseDto<ProductDetailResponseDto> findDetailProductById(@PathVariable long id) {
        return ResponseDto.<ProductDetailResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findDetailProductById(id))
                .build();
    }

    /**
     * [GET /products/{id}/manager] 요청을 받아 상품의 수정 View에 넣을 정보를 조회합니다.
     *
     * @param id 조회하고자 하는 상품의 Id
     * @return 조회한 상품의 상세를 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/manager")
    public ResponseDto<ProductModifyDto> findProductByIdForForm(@PathVariable long id) {
        return ResponseDto.<ProductModifyDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findProductByIdForForm(id))
                .build();
    }

    /**
     * [GET /products] 요청을 받아 상품을 모든 사용자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 상품의 페이징된 정보를 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping
    public ResponseDto<PaginatedResponseDto<ProductsResponseDto>> getProducts(
            Pageable pageable,
            @RequestParam(required = false) Integer typeId
    ) {
        return ResponseDto.<PaginatedResponseDto<ProductsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findAll(pageable, typeId))
                .build();
    }

    /**
     * [GET /products/manager] 요청을 받아 상품을 관리자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 상품의 페이징된 정보를 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/manager")
    public ResponseDto<PaginatedResponseDto<ProductsResponseDto>> getProductsForManager(
            Pageable pageable,
            @RequestParam(required = false) Integer typeId
    ) {
        return ResponseDto.<PaginatedResponseDto<ProductsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findAllForManager(pageable, typeId))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/manager", params = "title")
    public ResponseDto<PaginatedResponseDto<ProductsResponseDto>> getProductsByTitleForManager(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String title
    ) {
        return ResponseDto.<PaginatedResponseDto<ProductsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findByTitleForManager(title, pageable))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/manager", params = "content")
    public ResponseDto<PaginatedResponseDto<ProductsResponseDto>> getProductsByContentForManager(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String content
    ) {
        return ResponseDto.<PaginatedResponseDto<ProductsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findByContentForManager(content, pageable))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/manager", params = "publisher")
    public ResponseDto<PaginatedResponseDto<ProductsResponseDto>> getProductsByPublisherForManager(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String publisher
    ) {
        return ResponseDto.<PaginatedResponseDto<ProductsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findByPublisherForManager(publisher, pageable))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/manager", params = "author")
    public ResponseDto<PaginatedResponseDto<ProductsResponseDto>> getProductsByAuthorForManager(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String author
    ) {
        return ResponseDto.<PaginatedResponseDto<ProductsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findByAuthorForManager(author, pageable))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/manager", params = "isbn")
    public ResponseDto<PaginatedResponseDto<ProductsResponseDto>> getProductsByISBNForManager(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String isbn
    ) {
        return ResponseDto.<PaginatedResponseDto<ProductsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryProductService.findByISBNForManager(isbn, pageable))
                .build();
    }

    /**
     * 연관관계 등록을 위한 상품 검색 메서드
     *
     * @param title    검색할 상품 제목
     * @param pageable 페이지 정보
     * @return 검색된 상품 정보와 페이지 정보 그리고 응답 메시지
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}/relation", params = "title")
    public ResponseDto<PaginatedResponseDto<RelationsResponseDto>> findProductRelationByTitle(
            @PathVariable Long id,
            @RequestParam String title,
            @PageableDefault Pageable pageable
    ) {
        Page<RelationsResponseDto> products = queryProductService.findProductRelationByTitle(
                id,
                title,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<RelationsResponseDto>>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(PaginatedResponseDto.<RelationsResponseDto>builder()
                        .dataList(products.getContent())
                        .totalPage(products.getTotalPages())
                        .currentPage(products.getNumber())
                        .totalDataCount(products.getTotalElements())
                        .build())
                .build();
    }

    /**
     * 최신 상품 조회 메서드
     *
     * @param pageable 페이지 정보
     * @return 촤신 상품 리스트
     * @author 김선홍
     * @since 1, 0
     */
    @GetMapping("/recent/product")
    public ResponseDto<List<ProductRecentResponseDto>> findRecentProductByPublishedDate(
            @PageableDefault Pageable pageable
    ) {
        List<ProductRecentResponseDto> products = queryProductService.findRecentProductByPublishedDate(
                pageable);

        return ResponseDto.<List<ProductRecentResponseDto>>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(products)
                .build();
    }

    /**
     * 최근 본 상품 메서드
     *
     * @param dto      최근 본 상품 요청 dto
     * @param pageable 페이지 정보
     * @return 최근 본 상품 리스트
     * @author 김선홍
     * @since 1.0
     */
    @PostMapping("/recentview/product")
    public ResponseDto<PaginatedResponseDto<ProductRecentResponseDto>> findRecentViewProductById(
            @RequestBody RecentViewProductRequestDto dto,
            @PageableDefault Pageable pageable
    ) {
        Page<ProductRecentResponseDto> products = queryProductService.findRecentViewProductById(
                dto.getTotalIds(),
                dto.getPageIds(),
                pageable
        );
        return ResponseDto.<PaginatedResponseDto<ProductRecentResponseDto>>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(PaginatedResponseDto.<ProductRecentResponseDto>builder()
                        .dataList(products.getContent())
                        .totalPage(products.getTotalPages())
                        .currentPage(products.getNumber())
                        .totalDataCount(products.getTotalElements())
                        .build())
                .build();
    }
}
