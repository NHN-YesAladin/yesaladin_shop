package shop.yesaladin.shop.member.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : 송학현
 * @since : 1.0
 */
@Getter
@RequiredArgsConstructor
public enum MemberGrade {

    WHITE(1, "화이트", 0L, 0L),
    BRONZE(2, "브론즈", 100000L, 1000L),
    SILVER(3, "실버", 200000L, 2000L),
    GOLD(4, "골드", 300000L, 5000L),
    PLATINUM(5, "플래티넘", 500000L, 10000L);

    private final int id;
    private final String name;
    private final Long baseOrderAmount;
    private final Long baseGivenPoint;
}
