package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.product.dto.ProductTypeResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductTypeService;

import java.util.List;

/**
 * 상품 유형 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/product-types")
public class QueryProductTypeController {

    private final QueryProductTypeService queryProductTypeService;

    /**
     * [GET /product-types] 요청을 받아 상품유형 전체 조회합니다.
     *
     * @return 전체 조회한 상품유형들의 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<List<ProductTypeResponseDto>> getProductTypes() {
        return ResponseEntity.ok().body(queryProductTypeService.findAll());
    }
}
