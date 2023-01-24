package shop.yesaladin.shop.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

import java.util.List;

/**
 * 태그 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tags")
public class QueryTagController {

    private final QueryTagService queryTagService;

    /**
     * [GET /tags] 요청을 받아 태그를 전체 조회합니다.
     *
     * @return 전체 조회한 태그들의 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<List<TagsResponseDto>> getTags() {
        return ResponseEntity.ok().body(queryTagService.findAll());
    }
}