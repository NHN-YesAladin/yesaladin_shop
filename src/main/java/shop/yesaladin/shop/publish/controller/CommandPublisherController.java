package shop.yesaladin.shop.publish.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.shop.publish.dto.PublisherRequestDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 출판사 등록을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/publishers")
public class CommandPublisherController {

    private final CommandPublisherService commandPublisherService;

    /**
     * [POST /publishers] 요청을 받아 출판사를 생성하여 등록합니다.
     *
     * @param createDto 요청받은 출판사 정보(출판사 이름)
     * @return ResponseEntity
     * @author 이수정
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity registerPublisher(@Valid @RequestBody PublisherRequestDto createDto) throws URISyntaxException {
        PublisherResponseDto publisher = commandPublisherService.create(createDto);

        return ResponseEntity.created(new URI(publisher.getId().toString())).body(publisher);
    }

    /**
     * [PUT /publishers/{publisherId}] 요청을 받아 출판사를 수정합니다.
     *
     * @param modifyDto 요청받은 출판사 정보(저자 이름)
     * @return ResponseEntity
     * @author 이수정
     * @since 1.0
     */
    @PutMapping("/{publisherId}")
    public ResponseEntity modifyPublisher(@Valid @RequestBody PublisherRequestDto modifyDto, @PathVariable Long publisherId) {
        PublisherResponseDto publisher = commandPublisherService.modify(publisherId, modifyDto);

        return ResponseEntity.ok().body(publisher);
    }
}
