package shop.yesaladin.shop.category.dto;

import java.util.List;
import java.util.Objects;
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
            SearchedCategoryDtoBuilder searchedCategoryDto = SearchedCategoryDto.builder()
                    .id(searchedCategory.getId())
                    .name(searchedCategory.getName());
            if (Objects.isNull(searchedCategory.getParent())) {
                return searchedCategoryDto.build();
            }
            return searchedCategoryDto.parentName(searchedCategory.getParent().getName()).build();
        }
    }
}
