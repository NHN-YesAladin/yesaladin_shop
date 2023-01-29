package shop.yesaladin.shop.publish.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.dto.PublishersResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

import java.util.List;

/**
 * 출판사 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/publishers")
public class QueryPublisherController {

    private final QueryPublisherService queryPublisherService;

    /**
     * [GET /publishers] 요청을 받아 출판사를 전체 조회합니다.
     *
     * @return 전체 조회한 출판사들의 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<List<PublisherResponseDto>> getPublishers() {
        return ResponseEntity.ok().body(queryPublisherService.findAll());
    }

    /**
     * [GET /publishers/manager] 요청을 받아 출판사를 관리자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 출판사의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/manager")
    public PaginatedResponseDto<PublishersResponseDto> getPublishersForManager(Pageable pageable) {
        Page<PublishersResponseDto> publishers = queryPublisherService.findAllForManager(pageable);

        return PaginatedResponseDto.<PublishersResponseDto>builder()
                .totalPage(publishers.getTotalPages())
                .currentPage(publishers.getNumber())
                .totalDataCount(publishers.getTotalElements())
                .dataList(publishers.getContent())
                .build();
    }
}
