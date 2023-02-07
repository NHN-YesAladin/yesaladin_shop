package shop.yesaladin.shop.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 태그 전체 조회를 하여 dto로 반환합니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class TagsResponseDto {

    private Long id;
    private String name;
}
