package shop.yesaladin.shop.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.shop.tag.dto.TagRequestDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 태그 등록을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop", "https://test.yesaladin.shop"})
@RestController
@RequestMapping("/v1/tags")
public class CommandTagController {

    private final CommandTagService commandTagService;

    /**
     * [POST /tags] 요청을 받아 태그를 생성하여 등록합니다.
     *
     * @param createDto 요청받은 태그 정보(태그명)
     * @return ResponseEntity
     * @throws URISyntaxException URI 문법을 어긴 경우 던지는 예외
     * @author 이수정
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity registerTag(@Valid @RequestBody TagRequestDto createDto) throws URISyntaxException {
        TagResponseDto tag = commandTagService.create(createDto);

        return ResponseEntity.created(new URI(tag.getId().toString())).body(tag);
    }

    /**
     * [PUT /tags/{tagId}] 요청을 받아 태그를 수정합니다.
     *
     * @param modifyDto 요청받은 태그 정보(태그명)
     * @return ResponseEntity
     * @author 이수정
     * @since 1.0
     */
    @PutMapping("/{tagId}")
    public ResponseEntity modifyTag(@Valid @RequestBody TagRequestDto modifyDto, @PathVariable Long tagId) {
        TagResponseDto tag = commandTagService.modify(tagId, modifyDto);

        return ResponseEntity.ok().body(tag);
    }
}
