package shop.yesaladin.shop.member.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.exception.AlreadyBlockedMemberException;
import shop.yesaladin.shop.member.exception.AlreadyUnblockedMemberException;
import shop.yesaladin.shop.member.persistence.converter.MemberGenderCodeConverter;
import shop.yesaladin.shop.member.persistence.converter.MemberGradeCodeConverter;

/**
 * 회원의 엔티티 클래스 입니다.
 *
 * @author : 송학현, 최예린
 * @since : 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "members")
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 15, nullable = false)
    private String nickname;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "login_id", unique = true, length = 15, nullable = false)
    private String loginId;

    @Column(name = "login_password", nullable = false)
    private String password;

    @Column(name = "birth_year", nullable = false)
    private Integer birthYear;

    @Column(name = "birth_month", nullable = false)
    private Integer birthMonth;

    @Column(name = "birth_day", nullable = false)
    private Integer birthDay;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(length = 11, unique = true, nullable = false)
    private String phone;

    @Column(name = "sign_up_date", nullable = false)
    private LocalDate signUpDate;

    @Column(name = "withdrawal_date")
    private LocalDate withdrawalDate;

    @Column(name = "is_withdrawal", nullable = false)
    private boolean isWithdrawal;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "member_grade_id")
    @Convert(converter = MemberGradeCodeConverter.class)
    private MemberGrade memberGrade;

    @Column(name = "gender_code")
    @Convert(converter = MemberGenderCodeConverter.class)
    private MemberGenderCode memberGenderCode;

    /**
     * Member entity의 memberId 값을 비교 하는 기능 입니다.
     *
     * @param compare 비교 대상 Member entity 입니다.
     * @return loginId 가 중복 인지에 대한 결과
     * @author : 송학현
     * @since : 1.0
     */
    public boolean isSameLoginId(Member compare) {
        return Objects.equals(this.loginId, compare.getLoginId());
    }

    /**
     * Member entity 의 nickname 값을 비교 하는 기능 입니다.
     *
     * @param compare 비교 대상 Member entity 입니다.
     * @return nickname 이 중복 인지에 대한 결과
     * @author : 송학현
     * @since : 1.0
     */
    public boolean isSameNickname(Member compare) {
        return Objects.equals(this.nickname, compare.getNickname());
    }

    /**
     * Member entity 의 blocked 를 false 로 변경하기 위한 기능입니다.
     *
     * @author 최예린
     * @since 1.0
     */
    public void unblockMember() {
        if (!this.isBlocked) {
            throw new AlreadyUnblockedMemberException(this.id);
        }
        this.isBlocked = false;
    }

    /**
     * Member entity 의 blocked 를 true 로 변경하기 위한 기능입니다.
     *
     * @author 최예린
     * @since 1.0
     */
    public void blockMember() {
        if (this.isBlocked) {
            throw new AlreadyBlockedMemberException(this.id);
        }
        this.isBlocked = true;
    }

    /**
     * Member entity 의 nickname 을 수정하기 위한 기능입니다.
     *
     * @param newNickname 새로운 nickname
     * @author 최예린
     * @since 1.0
     */
    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    /**
     * Member entity를 soft delete 하기 위한 기능 입니다.
     *
     * @author : 송학현
     * @since : 1.0
     */
    public void withdrawMember() {
        String deleteUniqueField = "" + this.id;
        this.isWithdrawal = true;
        this.withdrawalDate = LocalDate.now();
        this.name = deleteUniqueField;
        this.nickname = deleteUniqueField;
        this.birthYear = 0;
        this.birthMonth = 0;
        this.birthDay = 0;
        this.phone = deleteUniqueField;
        this.password = "";
    }
}
