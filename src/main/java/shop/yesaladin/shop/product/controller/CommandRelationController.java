package shop.yesaladin.shop.product.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.RelationCreateDto;
import shop.yesaladin.shop.product.service.inter.CommandRelationService;

/**
 * 연관관계 등록/삭제을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop",
        "https://test.yesaladin.shop"})
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
            @Valid @RequestBody RelationCreateDto createDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Validation error in product relation register request." + bindingResult.getAllErrors()
            );
        }

        if (productMainId.equals(createDto.getProductSubId())) {
            throw new ClientException(
                    ErrorCode.PRODUCT_SELF_RELATE,
                    "product can't be related alone => main : " + productMainId + ", sub : "
                            + createDto.getProductSubId()
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
    public ResponseDto<Void> deleteRelation(
            @PathVariable Long productMainId,
            @PathVariable Long productSubId
    ) {
        commandRelationService.delete(productMainId, productSubId);

        return ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }
}
