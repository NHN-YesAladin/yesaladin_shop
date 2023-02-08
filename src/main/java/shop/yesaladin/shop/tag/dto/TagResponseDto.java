package shop.yesaladin.shop.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.tag.domain.model.Tag;

/**
 * 태그 데이터를 반환받는 DTO 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDto {

    private Long id;
    private String name;

    /**
     * Dto를 바탕으로 태그 엔터티를 생성해 반환합니다.
     *
     * @return 생성된 태그 엔터티
     * @author 이수정
     * @since 1.0
     */
    public Tag toEntity() {
        return Tag.builder().id(id).name(name).build();
    }

    /**
     * 태그관계를 바탕으로 태그 Dto를 만들어 반환합니다.
     *
     * @param productTag 바탕이 될 태그관계 Dto
     * @return 태그 Dto
     * @author 이수정
     * @since 1.0
     */
    public static TagResponseDto getTagFromProductTag(ProductTagResponseDto productTag) {
        return TagResponseDto.builder()
                .id(productTag.getTag().getId())
                .name(productTag.getTag().getName())
                .build();
    }
}
