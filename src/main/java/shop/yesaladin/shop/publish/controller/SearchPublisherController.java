package shop.yesaladin.shop.publish.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * @since : 1.0
     * @author : 김선홍
     */
    @GetMapping(params = "name")
    public SearchPublisherResponseDto searchPublisherByName(@ModelAttribute @Valid
            SearchPublisherRequestDto dto
    ) {
        return searchPublisherService.searchPublisherByName(dto);
    }
}
