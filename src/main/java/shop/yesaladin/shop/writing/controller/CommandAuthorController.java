package shop.yesaladin.shop.writing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.shop.writing.dto.AuthorRequestDto;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.service.inter.CommandAuthorService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 저자 등록을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/authors")
public class CommandAuthorController {

    private final CommandAuthorService commandAuthorService;

    /**
     * [POST /authors] 요청을 받아 저자를 생성하여 등록합니다.
     *
     * @param createDto 요청받은 저자 정보(저자 이름, 저자의 로그인 Id)
     * @return ResponseEntity
     * @author 이수정
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity registerAuthor(@Valid @RequestBody AuthorRequestDto createDto) throws URISyntaxException {
        AuthorResponseDto author = commandAuthorService.create(createDto);

        return ResponseEntity.created(new URI(author.getId().toString())).body(author);
    }

    /**
     * [PUT /authors/{authorId}] 요청을 받아 저자를 수정합니다.
     *
     * @param modifyDto 요청받은 저자 정보(저자 이름, 저자의 로그인 Id)
     * @return ResponseEntity
     * @author 이수정
     * @since 1.0
     */
    @PutMapping("/{authorId}")
    public ResponseEntity modifyAuthor(@Valid @RequestBody AuthorRequestDto modifyDto, @PathVariable Long authorId) {
        AuthorResponseDto author = commandAuthorService.modify(authorId, modifyDto);

        return ResponseEntity.ok().body(author);
    }
}
