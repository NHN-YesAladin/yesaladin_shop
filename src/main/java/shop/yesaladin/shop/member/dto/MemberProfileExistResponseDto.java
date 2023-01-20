package shop.yesaladin.shop.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원의 loginId, email, nickname과 같은 unique한 값들의
 * 중복 여부를 판별한 결과 정보를 담은 클래스 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
public class MemberProfileExistResponseDto {

    private boolean result;
}
