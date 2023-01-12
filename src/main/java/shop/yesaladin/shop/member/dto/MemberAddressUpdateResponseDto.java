package shop.yesaladin.shop.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;

/**
 * 회원의 대표 배송지를 설정한 후 반환되는 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberAddressUpdateResponseDto {

    private String address;
    private Member member;

    public static MemberAddressUpdateResponseDto fromEntity(MemberAddress memberAddress) {
        return new MemberAddressUpdateResponseDto(
                memberAddress.getAddress(),
                memberAddress.getMember()
        );
    }
}
