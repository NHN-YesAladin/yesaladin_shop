package shop.yesaladin.shop.order.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.order.dto.OrderInPeriodQueryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;

/**
 * 주문 데이터 조회에 관련된 기능을 수행하는 서비스 인터페이스
 *
 * @author 김홍대
 * @since 1.0
 */
public interface QueryOrderService {

    /**
     * 일정 기간 내의 페이지네이션 된 주문 데이터의 요약본을 가져옵니다.
     *
     * @param queryDto 검색 조건이 담겨있는 dto
     * @return 기간 내에 생성된 모든 주문 데이터의 요약본
     * @throws shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException 요청한 데이터 오프셋이 총
     *                                                                             데이터 수보다 클 경우 예외
     * @since 1.0
     */
    Page<OrderSummaryDto> getAllOrderListInPeriod(
            OrderInPeriodQueryDto queryDto,
            Pageable pageable
    );
}
