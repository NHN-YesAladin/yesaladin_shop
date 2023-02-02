package shop.yesaladin.shop.order.service.inter;

import shop.yesaladin.shop.order.dto.OrderCreateResponseDto;
import shop.yesaladin.shop.order.dto.OrderMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderNonMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderSubscribeCreateRequestDto;

/**
 * 주문 생성/수정/삭제 관련 서비스 interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandOrderService {

    /**
     * 비회원 주문을 생성합니다.
     *
     * @param request 주문 요청 데이터
     * @return 생성된 비회원 주문
     * @author 최예린
     * @since 1.0
     */
    OrderCreateResponseDto createNonMemberOrders(OrderNonMemberCreateRequestDto request);

    /**
     * 회원 주문을 생성합니다.
     *
     * @param request 주문 요청 데이터
     * @param loginId 회원의 아이디
     * @return 생성된 회원 주문
     * @author 최예린
     * @since 1.0
     */
    OrderCreateResponseDto createMemberOrders(
            OrderMemberCreateRequestDto request,
            String loginId
    );

    /**
     * 회원 주문을 생성합니다.
     *
     * @param request 주문 요청 데이터
     * @param loginId 회원의 아이디
     * @return 생성된 회원 주문
     * @author 최예린
     * @since 1.0
     */
    OrderCreateResponseDto createSubscribeOrders(
            OrderSubscribeCreateRequestDto request,
            String loginId
    );
}
