package shop.yesaladin.shop.publish.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.publish.dto.SearchPublisherRequestDto;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.SearchPublisherService;

/**
 * 엘라스틱서치 출판사 검색 컨트롤러
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search/publishers")
public class SearchPublisherController {

    private final SearchPublisherService searchPublisherService;

    /**
     * 출판사 이름으로 출판사를 검색하는 메서드
     *
     * @param dto 출판사 이름, 페이지 위치, 상품 갯수
     * @return 검색된 출판사 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping(params = "name")
    public ResponseDto<SearchPublisherResponseDto> searchPublisherByName(
            @ModelAttribute @Valid SearchPublisherRequestDto dto,
            BindingResult bindingResult

    ) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Validation Error in publisher search request." + bindingResult.getAllErrors()
            );
        }

        return ResponseDto.<SearchPublisherResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(searchPublisherService.searchPublisherByName(dto))
                .build();
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ResponseDto<Void>> clientExceptionHandler(ClientException ce) {
        return ResponseEntity.status(ce.getResponseStatus()).body(ResponseDto.<Void>builder()
                .success(true)
                .errorMessages(List.of(ce.getDisplayErrorMessage()))
                .status(ce.getResponseStatus())
                .build());
    }
}
