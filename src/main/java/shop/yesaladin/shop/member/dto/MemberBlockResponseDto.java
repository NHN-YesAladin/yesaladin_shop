package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 회원 차단 이후 MemberController 에서 클라이언트 에게 반환하기 위한 결과 DTO 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberBlockResponseDto {

    private Long id;
    private String name;
    private String loginId;
    private Boolean isBlocked;
    private LocalDate blockedDate;
    private String blockedReason;

    /**
     * Member Entity를 DTO로 변한하기 위한 메서드 입니다.
     *
     * @param member Repository 에서 등록된 결과 엔티티 입니다.
     * @return 결과로 반환된 엔티티의 일부 데이터를 사용하여 Response DTO로 반환합니다.
     * @author 최예린
     * @since 1.0
     */
    public static MemberBlockResponseDto fromEntity(Member member) {
        return new MemberBlockResponseDto(
                member.getId(),
                member.getName(),
                member.getLoginId(),
                member.isBlocked(),
                member.getBlockedDate(),
                member.getBlockedReason()
        );
    }
}
