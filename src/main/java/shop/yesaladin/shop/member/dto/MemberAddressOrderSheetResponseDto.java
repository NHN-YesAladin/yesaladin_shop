package shop.yesaladin.shop.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 주문서에 필요한 배송지 데이터를 조회하기 위한 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class MemberAddressOrderSheetResponseDto {

    private Long addressId;
    private String address;
}
