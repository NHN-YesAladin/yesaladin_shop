package shop.yesaladin.shop.member.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 회원 등급 엔티티 클래스 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */

//@Getter
//@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
//@Table(name = "member_grades")
//@Entity
@Getter
@RequiredArgsConstructor
public enum MemberGrade {

    WHITE(1, "화이트", 100000L, 1000L),
    SILVER(2, "실버", 200000L, 2000L),
    GOLD(3, "골드", 300000L, 5000L),
    PLATINUM(4, "플래티넘", 500000L, 10000L);

    private final int id;
    private final String name;
    private final Long baseOrderAmount;
    private final Long baseGivenPoint;

//    @Id
//    private Integer id;
//
//    @Column(nullable = false, length = 15)
//    private String name;
//
//    @Column(name = "base_order_amount", nullable = false)
//    private Long baseOrderAmount;
//
//    @Column(name = "base_given_point", nullable = false)
//    private Long baseGivenPoint;
}
