package shop.yesaladin.shop.member.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * 회원의 성별 코드에 대한 클래스입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum MemberGenderCode {
    MALE(1), FEMALE(2);

    private final int gender;
}
