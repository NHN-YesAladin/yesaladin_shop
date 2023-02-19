package shop.yesaladin.shop.order.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.order.domain.model.MemberOrder;

import java.time.LocalDateTime;

/**
 * 주문 관련 수정을 요청할 때 사용하는 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderUpdateResponseDto {

    private String orderNumber;
    private String name;
    private Boolean isHidden;
    private LocalDateTime changedDateTime;


    /**
     * 회원 주문 엔티티를 dto 클래스로 변환합니다.
     *
     * @param order 회원 주문
     * @return dto 클래스
     * @author 최예린
     * @since 1.0
     */
    public static OrderUpdateResponseDto fromEntity(MemberOrder order) {
        return new OrderUpdateResponseDto(
                order.getOrderNumber(),
                order.getName(),
                order.isHidden(),
                LocalDateTime.now()
        );
    }
}
