package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

/**
 * 상품 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
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
     * [GET /products] 요청을 받아 상품을 모든 사용자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 상품의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping
    public PaginatedResponseDto<ProductsResponseDto> getProducts(Pageable pageable, @RequestParam(required = false) Integer typeId) {
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
    public PaginatedResponseDto<ProductsResponseDto> getProductsForManager(Pageable pageable, @RequestParam(required = false) Integer typeId) {
        Page<ProductsResponseDto> products = queryProductService.findAllForManager(pageable, typeId);

        return PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(products.getTotalPages())
                .currentPage(products.getNumber())
                .totalDataCount(products.getTotalElements())
                .dataList(products.getContent())
                .build();
    }
}
