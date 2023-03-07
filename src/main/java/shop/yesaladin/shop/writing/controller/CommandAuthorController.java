package shop.yesaladin.shop.writing.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.writing.dto.AuthorRequestDto;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.service.inter.CommandAuthorService;

/**
 * 저자 등록을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop",
        "https://test.yesaladin.shop"})
@RestController
@RequestMapping("/v1/authors")
public class CommandAuthorController {

    private final CommandAuthorService commandAuthorService;

    /**
     * [POST /authors] 요청을 받아 저자를 생성하여 등록합니다.
     *
     * @param createDto 요청받은 저자 정보(저자 이름, 저자의 로그인 Id)
     * @return ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseDto<AuthorResponseDto> registerAuthor(
            @Valid @RequestBody AuthorRequestDto createDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Validation error in author register request." + bindingResult.getAllErrors()
            );
        }

        return ResponseDto.<AuthorResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(commandAuthorService.create(createDto))
                .build();
    }

    /**
     * [PUT /authors/{authorId}] 요청을 받아 저자를 수정합니다.
     *
     * @param modifyDto 요청받은 저자 정보(저자 이름, 저자의 로그인 Id)
     * @return ResponseEntity
     * @author 이수정
     * @since 1.0
     */
    @PutMapping("/{id}")
    public ResponseDto<AuthorResponseDto> modifyAuthor(
            @Valid @RequestBody AuthorRequestDto modifyDto,
            @PathVariable Long id,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Validation error in author modify request." + bindingResult.getAllErrors()
            );
        }

        return ResponseDto.<AuthorResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(commandAuthorService.modify(id, modifyDto))
                .build();
    }
}

