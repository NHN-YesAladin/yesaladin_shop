package shop.yesaladin.shop.order.service.inter;

import java.time.LocalDateTime;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.OrderCreateResponseDto;
import shop.yesaladin.shop.order.dto.OrderMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderNonMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderSubscribeCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderUpdateResponseDto;

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

    /**
     * 주문 숨김 기능을 관리합니다.
     *
     * @param loginId 회원 아이디
     * @param orderId 주문 pk
     * @param hide    숨김 여부
     * @return 주문의 상태
     * @author 최예린
     * @since 1.0
     */
    OrderUpdateResponseDto hideOnOrder(String loginId, Long orderId, boolean hide);

}
