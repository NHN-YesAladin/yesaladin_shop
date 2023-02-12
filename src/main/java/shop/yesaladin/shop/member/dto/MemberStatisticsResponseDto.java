package shop.yesaladin.shop.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 통계용 DTO 클래스 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberStatisticsResponseDto {

    private Long totalMembers;
    private Long totalBlockedMembers;
    private Long totalWithdrawMembers;

    private Long totalWhiteGrades;
    private Long totalBronzeGrades;
    private Long totalSilverGrades;
    private Long totalGoldGrades;
    private Long totalPlatinumGrades;
}
