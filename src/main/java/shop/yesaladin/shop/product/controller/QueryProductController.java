package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.product.dto.ProductModifyDto;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
import shop.yesaladin.shop.product.dto.ViewCartDto;
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
     * [GET /products/{productId}] 요청을 받아 상품을 상세 조회합니다.
     *
     * @param productId 조회하고자 하는 상품의 Id
     * @return 조회한 상품의 상세 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/{productId}")
    public ProductDetailResponseDto findProductById(@PathVariable long productId) {
        return queryProductService.findById(productId);
    }

    /**
     * [GET /products/{productId}/manager] 요청을 받아 상품의 수정 View에 넣을 정보를 조회합니다.
     *
     * @param productId 조회하고자 하는 상품의 Id
     * @return 조회한 상품의 상세 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/{productId}/manager")
    public ProductModifyDto findProductByIdForForm(@PathVariable long productId) {
        return queryProductService.findProductByIdForForm(productId);
    }

    /**
     * [GET /products] 요청을 받아 상품을 모든 사용자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 상품의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping
    public PaginatedResponseDto<ProductsResponseDto> getProducts(
            Pageable pageable,
            @RequestParam(required = false) Integer typeId
    ) {
        Page<ProductsResponseDto> products = queryProductService.findAll(pageable, typeId);

        return PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(products.getTotalPages())
                .currentPage(products.getNumber())
                .totalDataCount(products.getTotalElements())
                .dataList(products.getContent())
                .build();
    }

    /**
     * [GET /products/manager] 요청을 받아 상품을 관리자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 상품의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/manager")
    public PaginatedResponseDto<ProductsResponseDto> getProductsForManager(
            Pageable pageable,
            @RequestParam(required = false) Integer typeId
    ) {
        Page<ProductsResponseDto> products = queryProductService.findAllForManager(
                pageable,
                typeId
        );

        return PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(products.getTotalPages())
                .currentPage(products.getNumber())
                .totalDataCount(products.getTotalElements())
                .dataList(products.getContent())
                .build();
    }

    /**
     * [GET /products/cart] 요청을 받아 장바구니에 나타낼 상품의 정보를 조회합니다.
     *
     * @param cart 찾고자하는 Cart의 정보를 담은 Map
     * @return 정보조회한 List
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/cart")
    public List<ViewCartDto> getCartProducts(
            @RequestParam Map<String, String> cart
    ) {
        return queryProductService.getCartProduct(cart);
    }
}
