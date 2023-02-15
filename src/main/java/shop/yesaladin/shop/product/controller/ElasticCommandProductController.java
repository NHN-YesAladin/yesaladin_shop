package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.dto.SearchedProductUpdateDto;
import shop.yesaladin.shop.product.service.inter.ElasticCommandProductService;

/**
 * 엘라스틱서치에 상품을 수정, 삭제하는 컨트롤러
 *
 * @author 김선홍
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search/products")
public class ElasticCommandProductController {

    private final ElasticCommandProductService elasticCommandProductService;

    /**
     * 상품을 수정하는 메서드
     *
     * @param id 수정할 상품의 id
     * @param updateDto 수정할 상품의 정보
     * @return 수정 결과
     * @author 김선홍
     * @since 1.0
     */
    @PutMapping("/{id}")
    public ResponseDto<SearchedProduct> update(
            @PathVariable Long id,
            @RequestBody SearchedProductUpdateDto updateDto
    ) {
        return ResponseDto.<SearchedProduct>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(elasticCommandProductService.update(id, updateDto))
                .build();
    }

    /**
     * 상품을 삭제하는 메서드
     *
     * @param id 삭제할 상품의 id
     * @return 삭제 결과
     * @author 김선홍
     * @since 1.0
     */
    @PostMapping("/{id}")
    public ResponseDto<Void> delete(@PathVariable Long id) {
        elasticCommandProductService.delete(id);
        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }
}
