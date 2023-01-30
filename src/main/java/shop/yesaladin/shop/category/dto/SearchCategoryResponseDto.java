package shop.yesaladin.shop.category.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.category.domain.model.SearchedCategory;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCategoryResponseDto {

    Long count;
    List<SearchedCategoryDto> searchedCategoryDtoList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchedCategoryDto {

        Long id;
        String name;
        String parentName;

        public static SearchedCategoryDto fromIndex(SearchedCategory searchedCategory) {
            return SearchedCategoryDto.builder()
                    .id(searchedCategory.getId())
                    .name(searchedCategory.getName())
                    .parentName(searchedCategory.getParentName())
                    .build();
        }
    }
}
