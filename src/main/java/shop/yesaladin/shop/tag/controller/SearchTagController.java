package shop.yesaladin.shop.tag.controller;

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
import shop.yesaladin.shop.tag.dto.SearchTagRequestDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.SearchTagService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search/tags")
public class SearchTagController {

    private final SearchTagService searchTagService;

    @GetMapping(params = "name")
    public ResponseDto<SearchedTagResponseDto> searchByName(@ModelAttribute @Valid SearchTagRequestDto dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Validation Error in tag search request." + bindingResult.getAllErrors()
            );
        }
        return ResponseDto.<SearchedTagResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(searchTagService.searchTagByName(dto))
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
