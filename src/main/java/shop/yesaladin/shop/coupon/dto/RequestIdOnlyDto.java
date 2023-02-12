package shop.yesaladin.shop.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * requestId만을 가지는 DTO 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class RequestIdOnlyDto {

    private final String requestId;
}
