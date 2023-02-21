package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
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
@RequestMapping("/v1/products/{id}/relations")
public class QueryRelationController {

    private final QueryRelationService queryRelationService;

    /**
     * [GET /products/{id}/relations/manager] 요청을 받아 연관관계를 관리자용 Paging 전체 조회합니다.
     *
     * @param id       연관관계를 조회할 product의 Id
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 연관관계의 페이징된 정보까지 담은 ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/manager")
    public ResponseDto<PaginatedResponseDto<RelationsResponseDto>> getRelationsForManager(
            @PathVariable Long id,
            Pageable pageable
    ) {
        return ResponseDto.<PaginatedResponseDto<RelationsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryRelationService.findAllForManager(id, pageable))
                .build();
    }

    /**
     * [GET /products/{id}/relations] 요청을 받아 연관관계를 전체 사용자용 Paging 전체 조회합니다.
     *
     * @param id       연관관계를 조회할 product의 Id
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 연관관계의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping
    public ResponseDto<PaginatedResponseDto<RelationsResponseDto>> getRelations(
            @PathVariable Long id,
            Pageable pageable
    ) {
        return ResponseDto.<PaginatedResponseDto<RelationsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryRelationService.findAll(id, pageable))
                .build();
    }
}
