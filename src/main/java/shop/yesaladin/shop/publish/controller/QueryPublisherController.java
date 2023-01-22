package shop.yesaladin.shop.publish.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
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
}
