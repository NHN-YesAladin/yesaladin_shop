package shop.yesaladin.shop.coupon.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.coupon.domain.model.CouponSocketRequestKind;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponResultDto {

    private CouponSocketRequestKind requestKind;

    private String requestId;

    private boolean success;

    private String message;

}
