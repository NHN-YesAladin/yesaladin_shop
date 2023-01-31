package shop.yesaladin.shop.member.domain.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.persistence.converter.MemberGradeCodeConverter;

/**
 * 회원 등급 변동 이력을 저장할 엔티티 클래스 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member_grade_histories")
@Entity
public class MemberGradeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate;

    @Column(name = "previous_paid_amount", nullable = false)
    private Long previousPaidAmount;

    @Column(name = "grade_id")
    @Convert(converter = MemberGradeCodeConverter.class)
    private MemberGrade memberGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
