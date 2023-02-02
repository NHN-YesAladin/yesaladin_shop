package shop.yesaladin.shop.order.service.inter;

import org.springframework.security.core.userdetails.UserDetails;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.dto.OrderCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderCreateResponseDto;

/**
 * 주문 생성/수정/삭제 관련 서비스 interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandOrderService {

    /**
     * 주문을 생성합니다.
     *
     * @param orderCode   주문 코드
     * @param request     주문 요청 데이터
     * @param userDetails 인증정보
     * @return 생성된 회원의 주문 정보
     * @author 최예린
     * @since 1.0
     */
    OrderCreateResponseDto createOrderWith(
            OrderCode orderCode,
            OrderCreateRequestDto request,
            UserDetails userDetails
    );
}
