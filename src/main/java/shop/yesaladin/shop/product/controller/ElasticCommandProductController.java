package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
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
     * @return 수정 결과
     * @author 김선홍
     * @since 1.0
     */
    @PutMapping("/{id}")
    public ResponseDto<ProductOnlyIdDto> update(@PathVariable Long id) {
        return ResponseDto.<ProductOnlyIdDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(new ProductOnlyIdDto(elasticCommandProductService.update(id)))
                .build();
    }

    /**
     * 판매 여부 상태 변경 메서드
     *
     * @param id 수정할 상품의 id
     * @author 김선홍
     * @since 1.0
     */
    @GetMapping("/is-sale/{id}")
    public ResponseDto<ProductOnlyIdDto> changeIsSale(@PathVariable Long id) {
        return ResponseDto.<ProductOnlyIdDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(new ProductOnlyIdDto(elasticCommandProductService.changeIsSale(id)))
                .build();
    }

    /**
     * 강제 품절 상태 변경 메서드
     *
     * @param id 수정할 상품의 id
     * @author 김선홍
     * @since 1.0
     */
    @GetMapping("/is-forced-out-of-stock/{id}")
    public ResponseDto<ProductOnlyIdDto> changeIsForcedOutOfStock(@PathVariable Long id) {
        return ResponseDto.<ProductOnlyIdDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(new ProductOnlyIdDto(elasticCommandProductService.changeIsForcedOutOfStock(id)))
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
