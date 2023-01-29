package shop.yesaladin.shop.tag.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.service.inter.SearchTagService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search/tags")
public class SearchTagController {
    private final SearchTagService searchTagService;

    @GetMapping(params = "name")
    public List<TagsResponseDto> searchByName(@RequestParam String name) {
        return searchTagService.searchTagByName(name);
    }
}
