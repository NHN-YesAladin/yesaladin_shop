package shop.yesaladin.shop.tag.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.tag.dto.TagRequestDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;

/**
 * 태그 등록을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop",
        "https://test.yesaladin.shop"})
@RestController
@RequestMapping("/v1/tags")
public class CommandTagController {

    private final CommandTagService commandTagService;

    /**
     * [POST /tags] 요청을 받아 태그를 생성하여 등록합니다.
     *
     * @param createDto 요청받은 태그 정보(태그명)
     * @return ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseDto<TagResponseDto> registerTag(@Valid @RequestBody TagRequestDto createDto) {
        return ResponseDto.<TagResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(commandTagService.create(createDto))
                .build();
    }

    /**
     * [PUT /tags/{id}] 요청을 받아 태그를 수정합니다.
     *
     * @param modifyDto 요청받은 태그 정보(태그명)
     * @return ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseDto<TagResponseDto> modifyTag(
            @Valid @RequestBody TagRequestDto modifyDto,
            @PathVariable Long id
    ) {
        return ResponseDto.<TagResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(commandTagService.modify(id, modifyDto))
                .build();
    }
}
