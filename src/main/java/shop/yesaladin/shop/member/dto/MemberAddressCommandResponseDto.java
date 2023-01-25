package shop.yesaladin.shop.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.MemberAddress;

/**
 * 회원지 배송을 등록하고 난 결과값을 위한 반환 Dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberAddressCommandResponseDto {

    private Long id;
    private String address;

    private Boolean isDefault;
    private String loginId;

    public static MemberAddressCommandResponseDto fromEntity(MemberAddress memberAddress) {
        return new MemberAddressCommandResponseDto(
                memberAddress.getId(),
                memberAddress.getAddress(),
                memberAddress.isDefault(),
                memberAddress.getMember().getLoginId()
        );
    }
}
