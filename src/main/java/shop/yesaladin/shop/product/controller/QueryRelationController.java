package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dto.RelationsResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryRelationService;

/**
 * 연관관계 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop",
        "https://test.yesaladin.shop"})
@RestController
@RequestMapping("/v1/products/{productId}/relations")
public class QueryRelationController {

    private final QueryRelationService queryRelationService;

    /**
     * [GET /products/{productId}/relations/manager] 요청을 받아 연관관계를 관리자용 Paging 전체 조회합니다.
     *
     * @param productId 연관관계를 조회할 product의 Id
     * @param pageable  페이징 처리를 위한 객체
     * @return 조회한 연관관계의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manager")
    public PaginatedResponseDto<RelationsResponseDto> getRelationsForManager(
            @PathVariable Long productId,
            Pageable pageable
    ) {
        Page<RelationsResponseDto> relations = queryRelationService.findAllForManager(
                productId,
                pageable
        );

        return PaginatedResponseDto.<RelationsResponseDto>builder()
                .totalPage(relations.getTotalPages())
                .currentPage(relations.getNumber())
                .totalDataCount(relations.getTotalElements())
                .dataList(relations.getContent())
                .build();
    }

    /**
     * [GET /products/{productId}/relations] 요청을 받아 연관관계를 전체 사용자용 Paging 전체 조회합니다.
     *
     * @param productId 연관관계를 조회할 product의 Id
     * @param pageable  페이징 처리를 위한 객체
     * @return 조회한 연관관계의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public PaginatedResponseDto<RelationsResponseDto> getRelations(
            @PathVariable Long productId,
            Pageable pageable
    ) {
        Page<RelationsResponseDto> relations = queryRelationService.findAll(productId, pageable);

        return PaginatedResponseDto.<RelationsResponseDto>builder()
                .totalPage(relations.getTotalPages())
                .currentPage(relations.getNumber())
                .totalDataCount(relations.getTotalElements())
                .dataList(relations.getContent())
                .build();
    }
}
