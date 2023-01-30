package shop.yesaladin.shop.writing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

import java.util.List;

/**
 * 저자 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/authors")
public class QueryAuthorController {

    private final QueryAuthorService queryAuthorService;

    /**
     * [GET /authors] 요청을 받아 저자를 전체 조회합니다.
     *
     * @return 전체 조회한 저자들의 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping
    public List<AuthorsResponseDto> getAuthors() {
        return queryAuthorService.findAll();
    }

    /**
     * [GET /authors/manager] 요청을 받아 저자를 관리자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 저자의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/manager")
    public PaginatedResponseDto<AuthorsResponseDto> getAuthorsForManager(Pageable pageable) {
        Page<AuthorsResponseDto> authors = queryAuthorService.findAllForManager(pageable);

        return PaginatedResponseDto.<AuthorsResponseDto>builder()
                .totalPage(authors.getTotalPages())
                .currentPage(authors.getNumber())
                .totalDataCount(authors.getTotalElements())
                .dataList(authors.getContent())
                .build();
    }
}
