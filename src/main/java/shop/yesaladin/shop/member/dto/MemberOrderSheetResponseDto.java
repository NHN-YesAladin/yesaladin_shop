package shop.yesaladin.shop.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 주문서에 필요한 회원 정보를 반환하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class MemberOrderSheetResponseDto {

    private String name;
    private String phoneNumber;
    private int couponCount;
}
