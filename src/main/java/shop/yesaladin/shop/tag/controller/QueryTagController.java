package shop.yesaladin.shop.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

/**
 * 태그 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tags")
public class QueryTagController {

    private final QueryTagService queryTagService;

    /**
     * [GET /tags/manager] 요청을 받아 태그를 관리자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 태그의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/manager")
    public ResponseDto<PaginatedResponseDto<TagResponseDto>> getTagsForManager(Pageable pageable) {
        return ResponseDto.<PaginatedResponseDto<TagResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryTagService.findAllForManager(pageable))
                .build();
    }
}
