package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

/**
 * 회원 등급변경내역을 조회할 때 사용하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberGradeHistoryQueryResponseDto {

    private Long id;
    private LocalDate updateDate;
    private Long previousPaidAmount;
    private MemberGrade memberGrade;
    private String loginId;
}
