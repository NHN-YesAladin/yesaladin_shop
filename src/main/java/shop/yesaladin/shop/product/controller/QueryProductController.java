package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dto.*;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

import java.util.List;
import java.util.Map;

/**
 * 상품 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
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
}
