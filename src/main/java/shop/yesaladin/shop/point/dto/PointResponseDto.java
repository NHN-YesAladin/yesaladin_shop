package shop.yesaladin.shop.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원의 포인트를 반환하는 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class PointResponseDto {

    Long amount;
}
