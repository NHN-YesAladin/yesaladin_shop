package shop.yesaladin.shop.publish.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.publish.domain.model.SearchedPublisher;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPublisherResponseDto {

    Long count;
    List<SearchedPublisherDto> searchedPublisherDtoList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchedPublisherDto {

        Long id;
        String name;

        public static SearchedPublisherDto fromIndex(SearchedPublisher searchedPublisher) {
            return SearchedPublisherDto.builder()
                    .id(searchedPublisher.getId())
                    .name(searchedPublisher.getName())
                    .build();
        }
    }
}
