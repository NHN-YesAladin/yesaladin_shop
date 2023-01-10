package shop.yesaladin.shop.tag.dto;

import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.yesaladin.shop.tag.domain.model.Tag;

/**
 * 태그 데이터를 반환받는 DTO 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TagResponseDto {
    private Long id;
    private String name;

    public Tag toEntity() {
        return Tag.builder().id(id).name(name).build();
    }
}
