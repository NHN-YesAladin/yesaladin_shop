package shop.yesaladin.shop.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.tag.domain.model.SearchedTag;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchedTagResponseDto {

    Long count;
    List<SearchedTagDto> searchedTagDtoList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchedTagDto {

        Long id;
        String name;

        public static SearchedTagDto fromIndex(SearchedTag searchedTag) {
            return SearchedTagDto.builder()
                    .id(searchedTag.getId())
                    .name(searchedTag.getName())
                    .build();
        }
    }
}
