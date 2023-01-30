package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 회원의 정보를 반환하는 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberQueryResponseDto {

    private Long id;
    private String nickname;
    private String name;
    private String loginId;
    private String password;
    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;
    private String email;
    private LocalDate signUpDate;
    private String grade;
    private String gender;

    /**
     * 회원 엔티티 클래스를 회원 조회 dto 클래스로 반환합니다.
     *
     * @param member 회원 엔티티
     * @return 회원 조회 반환 dto 클래스
     * @author 최예린
     * @since 1.0
     */
    public static MemberQueryResponseDto fromEntity(Member member) {
        return new MemberQueryResponseDto(
                member.getId(),
                member.getNickname(),
                member.getName(),
                member.getLoginId(),
                member.getPassword(),
                member.getBirthYear(),
                member.getBirthMonth(),
                member.getBirthDay(),
                member.getEmail(),
                member.getSignUpDate(),
                member.getMemberGrade().getName(),
                member.getMemberGenderCode().getGender() == 1 ? "남" : "여"
        );
    }
}
