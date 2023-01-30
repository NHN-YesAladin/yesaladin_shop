package shop.yesaladin.shop.tag.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.tag.dto.SearchTagRequestDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.SearchTagService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search/tags")
public class SearchTagController {
    private final SearchTagService searchTagService;

    @GetMapping(params = "name")
    public SearchedTagResponseDto searchByName(@ModelAttribute @Valid SearchTagRequestDto dto) {
        return searchTagService.searchTagByName(dto);
    }
}
