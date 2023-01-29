package shop.yesaladin.shop.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
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
    public List<TagsResponseDto> getTags() {
        return queryTagService.findAll();
    }

    /**
     * [GET /tags/manager] 요청을 받아 태그를 관리자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 태그의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/manager")
    public PaginatedResponseDto<TagsResponseDto> getTagsForManager(Pageable pageable) {
        Page<TagsResponseDto> tags = queryTagService.findAllForManager(pageable);

        return PaginatedResponseDto.<TagsResponseDto>builder()
                .totalPage(tags.getTotalPages())
                .currentPage(tags.getNumber())
                .totalDataCount(tags.getTotalElements())
                .dataList(tags.getContent())
                .build();
    }
}
