package shop.yesaladin.shop.member.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 등급 엔티티 클래스 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member_grades")
@Entity
public class MemberGrade {

    @Id
    private Integer id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(name = "base_order_amount", nullable = false)
    private Long baseOrderAmount;

    @Column(name = "base_given_point", nullable = false)
    private Long baseGivenPoint;
}
