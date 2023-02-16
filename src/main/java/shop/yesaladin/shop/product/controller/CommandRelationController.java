package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.RelationCreateDto;
import shop.yesaladin.shop.product.service.inter.CommandRelationService;

import javax.validation.Valid;

/**
 * 연관관계 등록/삭제을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop", "https://test.yesaladin.shop"})
@RestController
@RequestMapping("/v1/products/{productMainId}/relations")
public class CommandRelationController {

    private final CommandRelationService commandRelationService;

    /**
     * [POST /products/{productMainId}/relations] 요청을 받아 연관관계를 생성하여 등록합니다.
     *
     * @param productMainId 연관관계를 연결할 메인 대상 상품
     * @return 생성된 연관관계 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseDto<ProductOnlyIdDto> registerRelation(
            @PathVariable Long productMainId,
            @Valid @RequestBody RelationCreateDto createDto
    ) {
        if (productMainId.equals(createDto.getProductSubId())) {
            throw new ClientException(
                    ErrorCode.PRODUCT_SELF_RELATE,
                    "product can't be related alone => main : " + productMainId + ", sub : " + createDto.getProductSubId()
            );
        }
        return ResponseDto.<ProductOnlyIdDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(commandRelationService.create(productMainId, createDto.getProductSubId()))
                .build();
    }

    /**
     * [DELETE /products/{productMainId}/relations/{ProductSubId}] 요청을 받아 연관관계를 삭제합니다.
     *
     * @param productMainId 연관관계를 해제할 메인 대상 상품
     * @param productSubId  연관관계를 해제할 서브 대상 상품
     * @author 이수정
     * @since 1.0
     */
    @DeleteMapping("/{productSubId}")
    public ResponseDto<Void> deleteRelation(@PathVariable Long productMainId, @PathVariable Long productSubId) {
        commandRelationService.delete(productMainId, productSubId);

        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }
}
