package shop.yesaladin.shop.writing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 저자 전체 조회를 하여 Dto로 반환합니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorsResponseDto {

    private Long id;
    private String name;
    private String loginId;
}
