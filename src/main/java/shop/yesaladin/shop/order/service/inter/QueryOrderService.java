package shop.yesaladin.shop.order.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;

/**
 * 주문 데이터 조회에 관련된 기능을 수행하는 서비스 인터페이스
 *
 * @author 김홍대
 * @author 배수한
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
     * @throws PageOffsetOutOfBoundsException 요청한 데이터 오프셋이 총 데이터 수보다 클 경우 예외
     * @throws MemberNotFoundException        요청한 회원 ID가 존재하지 않을 경우 예외
     * @author 김홍대
     * @since 1.0
     */
    Page<OrderSummaryDto> getAllOrderListInPeriodByMemberId(
            PeriodQueryRequestDto queryDto,
            long memberId,
            Pageable pageable
    );

    /**
     * 주문 번호로 주문 정보를 조회합니다.
     *   결제 서비스와 연동하여 내부적으로 사용
     *
     * @param number 주문 번호
     * @return 주문 정보 엔티티
     * @author 배수한
     * @since 1.0
     */
    Order getOrderByNumber(String number);

    /**
     * 특정 회원의 일정 기간 내 페이지네이션 된 주문 데이터의 요약본을 가져옵니다.
     *
     * @param queryDto 검색 조건이 담겨있는 dto
     * @return 특정 회원의 기간 내에 생성된 모든 주문 데이터의 요약본
     * @throws PageOffsetOutOfBoundsException 요청한 데이터 오프셋이 총 데이터 수보다 클 경우 예외
     * @throws MemberNotFoundException        요청한 회원 ID가 존재하지 않을 경우 예외
     */
    Page<OrderSummaryResponseDto> getOrderListInPeriodByMemberId(
            PeriodQueryRequestDto queryDto,
            long memberId,
            Pageable pageable
    );

}
