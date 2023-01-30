package shop.yesaladin.shop.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 회원의 등급을 조회 후 반환하는 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberGradeQueryResponseDto {

    String gradeEn;
    String gradeKo;

    public static MemberGradeQueryResponseDto fromEntity(Member member) {
        return new MemberGradeQueryResponseDto(
                member.getMemberGrade().name(),
                member.getMemberGrade().getName()
        );
    }
}
