package shop.yesaladin.shop.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shop.yesaladin.shop.member.domain.model.Member;
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
public class MemberAddressCreateResponseDto {
    private Long id;
    private String address;

    private Boolean isDefault;
    private Member member;

    public static MemberAddressCreateResponseDto fromEntity(MemberAddress memberAddress) {
        return new MemberAddressCreateResponseDto(
                memberAddress.getId(),
                memberAddress.getAddress(),
                memberAddress.isDefault(),
                memberAddress.getMember()
        );
    }
}
