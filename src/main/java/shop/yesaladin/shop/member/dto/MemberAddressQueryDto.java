package shop.yesaladin.shop.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;

/**
 * 회원읩 배송지 목록을 조회하는데 사용하는 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberAddressQueryDto {

    private Long id;
    private String address;
    private boolean isDefault;
    private Member member;

    /**
     * MemberAddress를 MemberAddressQueryDto 객체로 변환해주는 기능입니다.
     *
     * @param memberAddress memberAddress 엔티티
     * @return MemberAddressQueryDto 객체
     * @author 최예린
     * @since 1.0
     */
    public static MemberAddressQueryDto fromEntity(MemberAddress memberAddress) {
        return new MemberAddressQueryDto(
                memberAddress.getId(),
                memberAddress.getAddress(),
                memberAddress.isDefault(),
                memberAddress.getMember()
        );
    }
}
