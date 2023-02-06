package shop.yesaladin.shop.point.service.inter;

import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;

/**
 * 포인트내역 등록 관련 service interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandPointHistoryService {

    /**
     * 포인트 사용 내역을 등록합니다.
     *
     * @param request 사용한 포인트 양
     * @return 등록된 포인트내역
     * @author 최예린
     * @since 1.0
     */
    PointHistoryResponseDto use(PointHistoryRequestDto request);

    /**
     * 포인트 적립 내역을 등록합니다.
     *
     * @param request 적립한 포인트 양
     * @return 등록된 포인트내역
     * @author 최예린
     * @since 1.0
     */
    PointHistoryResponseDto save(PointHistoryRequestDto request);

    /**
     * 포인트 집계 내역을 등록합니다.
     *
     * @param request 집계한 포인트
     * @return 집계된 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    PointHistoryResponseDto sum(PointHistoryRequestDto request);
}
