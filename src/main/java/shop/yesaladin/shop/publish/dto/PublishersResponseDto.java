package shop.yesaladin.shop.publish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 출판사 전체 조회를 하여 Dto로 반환합니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishersResponseDto {

    private Long id;
    private String name;
}
