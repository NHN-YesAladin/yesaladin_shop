package shop.yesaladin.shop.order.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 비회원의 정보를 담아 요청하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NonMemberRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;
}
