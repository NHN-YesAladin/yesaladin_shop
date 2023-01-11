package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;

/**
 * 회원 등급변경내역을 조회할 때 사용하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberGradeHistoryQueryResponseDto {

    private Long id;
    private LocalDate updateDate;
    private Long previousPaidAmount;
    private MemberGrade memberGrade;
    private Member member;

    /**
     * 회원등급변경 내역 dto 클래스를 entity 로 변환합니다.
     *
     * @return 회원등급면경 내역 entity
     * @author 최예린
     * @since 1.0
     */
    public MemberGradeHistory toEntity() {
        return MemberGradeHistory.builder()
                .id(id)
                .updateDate(updateDate)
                .previousPaidAmount(previousPaidAmount)
                .memberGrade(memberGrade)
                .member(member)
                .build();
    }
}
