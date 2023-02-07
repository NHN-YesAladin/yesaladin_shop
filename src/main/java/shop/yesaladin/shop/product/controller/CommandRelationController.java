package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.RelationCreateDto;
import shop.yesaladin.shop.product.exception.SelfRelateException;
import shop.yesaladin.shop.product.service.inter.CommandRelationService;

import javax.validation.Valid;

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
     * @return 생성된 연관관계 Dto
     * @author 이수정
     * @since 1.0
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductOnlyIdDto registerRelation(
            @PathVariable Long productMainId,
            @Valid @RequestBody RelationCreateDto createDto
    ) {
        if (productMainId.equals(createDto.getProductSubId())) {
            throw new SelfRelateException(productMainId);
        }
        return commandRelationService.create(productMainId, createDto.getProductSubId());
    }

    /**
     * [DELETE /products/{productMainId}/relations/{ProductSubId}] 요청을 받아 연관관계를 삭제합니다.
     *
     * @param productMainId 연관관계를 해제할 메인 대상 상품
     * @param productSubId  연관관계를 해제할 서브 대상 상품
     * @author 이수정
     * @since 1.0
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{productSubId}")
    public void deleteRelation(@PathVariable Long productMainId, @PathVariable Long productSubId) {
        commandRelationService.delete(productMainId, productSubId);
    }
}
