package shop.yesaladin.shop.tag.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;
import shop.yesaladin.shop.tag.dto.SearchTagRequestDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.SearchTagService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search/tags")
public class SearchTagController {
    private final SearchTagService searchTagService;

    @GetMapping(params = "name")
    public ResponseDto<SearchedTagResponseDto> searchByName(@ModelAttribute @Valid SearchTagRequestDto dto) {
        return ResponseDto.<SearchedTagResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(searchTagService.searchTagByName(dto))
                .build();
    }
}
