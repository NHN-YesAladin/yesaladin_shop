package shop.yesaladin.shop.writing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

import java.util.List;

/**
 * 저자 조회를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/v1/authors")
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
    public ResponseEntity<List<AuthorsResponseDto>> getAuthors() {
        return ResponseEntity.ok().body(queryAuthorService.findAll());
    }
}
