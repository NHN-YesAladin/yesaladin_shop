package shop.yesaladin.shop.order.service.inter;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.OrderDetailsResponseDto;
import shop.yesaladin.shop.order.dto.OrderPaymentResponseDto;
import shop.yesaladin.shop.order.dto.OrderSheetRequestDto;
import shop.yesaladin.shop.order.dto.OrderSheetResponseDto;
import shop.yesaladin.shop.order.dto.OrderStatusResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;

/**
 * 주문 데이터 조회에 관련된 기능을 수행하는 서비스 인터페이스
 *
 * @author 김홍대
 * @author 배수한
 * @author 최예린
 * @since 1.0
 */
public interface QueryOrderService {

    /**
     * 일정 기간 내의 페이지네이션 된 주문 데이터의 요약본을 가져옵니다.
     *
     * @param queryDto 검색 조건이 담겨있는 dto
     * @return 기간 내에 생성된 모든 주문 데이터의 요약본
     * @throws PageOffsetOutOfBoundsException 요청한 데이터 오프셋이 총 데이터 수보다 클 경우 예외
     * @author 김홍대
     * @since 1.0
     */
    Page<OrderSummaryDto> getAllOrderListInPeriod(
            PeriodQueryRequestDto queryDto,
            Pageable pageable
    );

    /**
     * 특정 회원의 일정 기간 내 페이지네이션 된 주문 데이터의 요약본을 가져옵니다.
     *
     * @param queryDto 검색 조건이 담겨있는 dto
     * @return 특정 회원의 기간 내에 생성된 모든 주문 데이터의 요약본
     * @throws PageOffsetOutOfBoundsException                  요청한 데이터 오프셋이 총 데이터 수보다 클 경우 예외
     * @throws shop.yesaladin.common.exception.ClientException 요청한 회원 ID가 존재하지 않을 경우 예외
     * @author 김홍대
     * @since 1.0
     */
    Page<OrderSummaryDto> getAllOrderListInPeriodByMemberId(
            PeriodQueryRequestDto queryDto,
            long memberId,
            Pageable pageable
    );

    /**
     * 주문 번호로 주문 정보를 조회합니다. 결제 서비스와 연동하여 내부적으로 사용
     *
     * @param number 주문 번호
     * @return 주문 정보 엔티티
     * @author 배수한
     * @since 1.0
     */
    Order getOrderByNumber(String number);

    /**
     * 회원주문의 주문서에 필요한 데이터를 조회합니다.
     *
     * @param request 주문 상품 목록
     * @param loginId 회원의 아이디
     * @return 주문에 필요한 데이터
     * @author 최예린
     * @since 1.0
     */
    OrderSheetResponseDto getMemberOrderSheetData(OrderSheetRequestDto request, String loginId);

    /**
     * 비회원주문의 주문서에 필요한 데이터를 조회합니다.
     *
     * @param request 주문 상품 목록
     * @return 주문에 필요한 데이터
     * @author 최예린
     * @since 1.0
     */
    OrderSheetResponseDto getNonMemberOrderSheetData(OrderSheetRequestDto request);

    /**
     * 특정 회원의 일정 기간 내 페이지네이션 된 주문 데이터의 요약본을 가져옵니다.
     *
     * @param queryDto 검색 조건이 담겨있는 dto
     * @return 특정 회원의 기간 내에 생성된 모든 주문 데이터의 요약본
     * @throws PageOffsetOutOfBoundsException                  요청한 데이터 오프셋이 총 데이터 수보다 클 경우 예외
     * @throws shop.yesaladin.common.exception.ClientException 요청한 회원 ID가 존재하지 않을 경우 예외
     * @author 배수한
     * @since 1.0
     */
    Page<OrderSummaryResponseDto> getOrderListInPeriodByMemberId(
            PeriodQueryRequestDto queryDto,
            String loginId,
            Pageable pageable
    );

    /**
     * 결제 이후 정보 중 주문 pk를 통해서 주문자 이름과 주소를 조회합니다.
     *
     * @param orderId 주문 pk
     * @return 주문자 이름, 주문 주소지
     * @author 배수한
     * @since 1.0
     */
    OrderPaymentResponseDto getPaymentDtoByMemberOrderId(long orderId);

    /**
     * 주문 상태와 로그인 아이디에 맞춰 주문을 조회합니다.
     *
     * @param loginId  로그인 id
     * @param code     주문 상태
     * @param pageable 페이징 처리
     * @return 페이징된 주문의 일부 정보
     */
    Page<OrderStatusResponseDto> getStatusResponsesByLoginIdAndStatus(
            String loginId,
            OrderStatusCode code,
            Pageable pageable
    );

    /**
     * 주문상태에 따른 주문 개수 조회
     *
     * @param loginId 로그인 id
     * @return 주문상태코드 & 그에 따른 주문 개수
     */
    Map<OrderStatusCode, Long> getOrderCountByLoginIdStatus(String loginId);

    /**
     * 회원의 숨김처리된 주문 내역 반환합니다.
     *
     * @param loginId  회원의 아이디
     * @param pageable 페이지와 사이즈
     * @return 회원의 숨김처리된 주문 내역
     * @author 최예린
     * @since 1.0
     */
    Page<OrderSummaryResponseDto> getHiddenOrderByLoginId(String loginId, Pageable pageable);

    /**
     * 주문 상세 정보를 위한 조회
     *
     * @param orderNumber 상세 조회를 할 주문 번호
     * @return 주문 상세 정보
     */
    OrderDetailsResponseDto getDetailsDtoByOrderNumber(String orderNumber);
}
