package shop.yesaladin.shop.writing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

/**
 * 저자 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop", "https://test.yesaladin.shop"})
@RestController
@RequestMapping("/v1/authors")
public class QueryAuthorController {

    private final QueryAuthorService queryAuthorService;

    /**
     * [GET /authors/manager] 요청을 받아 저자를 관리자용 Paging 전체 조회합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 조회한 저자의 페이징된 정보까지 담은 dto
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/manager")
    public ResponseDto<PaginatedResponseDto<AuthorsResponseDto>> getAuthorsForManager(Pageable pageable) {
        return ResponseDto.<PaginatedResponseDto<AuthorsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryAuthorService.findAllForManager(pageable))
                .build();
    }
}
