package shop.yesaladin.shop.writing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.writing.domain.model.SearchedAuthor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchedAuthorResponseDto {

    Long count;
    List<SearchedAuthorDto> searchedAuthorDtoList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchedAuthorDto {

        Long id;
        String name;
        String loginId;

        public static SearchedAuthorDto fromIndex(SearchedAuthor searchedAuthor) {
            return SearchedAuthorDto.builder()
                    .id(searchedAuthor.getId())
                    .name(searchedAuthor.getName())
                    .loginId(searchedAuthor.getLoginId())
                    .build();
        }
    }
}
