package shop.yesaladin.shop.publish.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherRequestDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;

import javax.validation.Valid;

/**
 * 출판사 등록을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop", "https://test.yesaladin.shop"})
@RestController
@RequestMapping("/v1/publishers")
public class CommandPublisherController {

    private final CommandPublisherService commandPublisherService;

    /**
     * [POST /publishers] 요청을 받아 출판사를 생성하여 등록합니다.
     *
     * @param createDto 요청받은 출판사 정보(출판사 이름)
     * @return ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseDto<PublisherResponseDto> registerPublisher(@Valid @RequestBody PublisherRequestDto createDto) {
        return ResponseDto.<PublisherResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(commandPublisherService.create(createDto))
                .build();
    }

    /**
     * [PUT /publishers/{id}] 요청을 받아 출판사를 수정합니다.
     *
     * @param modifyDto 요청받은 출판사 정보(출판사 이름)
     * @param id        요청받은 출판사의 아이디
     * @return ResponseDto
     * @author 이수정
     * @since 1.0
     */
    @PutMapping("/{id}")
    public ResponseDto<PublisherResponseDto> modifyPublisher(
            @Valid @RequestBody PublisherRequestDto modifyDto,
            @PathVariable Long id
    ) {
        return ResponseDto.<PublisherResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(commandPublisherService.modify(id, modifyDto))
                .build();
    }
}
