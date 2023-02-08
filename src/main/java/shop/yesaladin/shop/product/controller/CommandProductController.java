package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.ProductUpdateDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;

import javax.validation.Valid;

/**
 * 상품 등록/수정/삭제를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
public class CommandProductController {

    private final CommandProductService commandProductService;

    /**
     * [POST /products] 요청을 받아 상품을 생성하여 등록합니다.
     *
     * @param createDto 관리자에게서 입력받은 상품을 생성하기 위한 요청 파라미터
     * @return 생성된 상품 정보를 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseDto<ProductOnlyIdDto> registerProduct(@Valid @RequestBody ProductCreateDto createDto) {
        return ResponseDto.<ProductOnlyIdDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(commandProductService.create(createDto))
                .build();
    }

    /**
     * [PUT /products/{id}] 요청을 받아 상품 정보를 수정합니다.
     *
     * @param id        수정할 상품의 Id
     * @param updateDto 상품 정보 수정을 위한 Dto
     * @return 수정된 상품 정보를 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @PutMapping("/{id}")
    public ResponseDto<ProductOnlyIdDto> updateProduct(
            @PathVariable("id") long id,
            @Valid @RequestBody ProductUpdateDto updateDto
    ) {
        return ResponseDto.<ProductOnlyIdDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(commandProductService.update(id, updateDto))
                .build();
    }

    /**
     * [POST /products/{id}] 요청을 받아 상품을 삭제상태로 변경합니다.
     *
     * @param id 삭제할 상품의 Id
     * @author 이수정
     * @since 1.0
     */
    @PostMapping("/{id}")
    public ResponseDto<Void> deleteProduct(@PathVariable("id") long id) {
        commandProductService.softDelete(id);

        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * [POST /products/{id}/is-sale] 요청을 받아 상품의 판매여부를 변경합니다.
     *
     * @param id 판매여부를 변경할 상품의 Id
     * @author 이수정
     * @since 1.0
     */
    @PostMapping("/{id}/is-sale")
    public ResponseDto<Void> changeProductIsSale(@PathVariable("id") long id) {
        commandProductService.changeIsSale(id);

        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * [POST /products/{id}/is-forced-out-of-stock] 요청을 받아 상품의 강제품절여부를 변경합니다.
     *
     * @param id 강제품절여부를 변경할 상품의 Id
     * @author 이수정
     * @since 1.0
     */
    @PostMapping("/{id}/is-forced-out-of-stock")
    public ResponseDto<Void> changeProductIsForcedOutOfStock(@PathVariable("id") long id) {
        commandProductService.changeIsForcedOutOfStock(id);

        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }
}
