package shop.yesaladin.shop.point.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;

/**
 * 회원의 포인트 내역 조회를 위한 service 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryPointHistoryService {

    /**
     * 회원의 포인트 사용/적립 내역을 조회합니다.
     *
     * @param loginId   회원의 아이디
     * @param pointCode 사용/적립 구분
     * @param pageable  페이지와 사이즈
     * @return 회원의 포인트 사용/적립 내역
     * @author 최예린
     * @since 1.0
     */
    Page<PointHistoryResponseDto> getPointHistoriesWithLoginIdAndCode(
            String loginId,
            PointCode pointCode,
            Pageable pageable
    );

    /**
     * 회원의 전체 포인트 내역을 조회합니다.
     *
     * @param loginId  회원의 아이디
     * @param pageable 페이지와 사이즈
     * @return 회원의 포인트 사용/적립 내역
     * @author 최예린
     * @since 1.0
     */
    Page<PointHistoryResponseDto> getPointHistoriesWithLoginId(String loginId, Pageable pageable);

    /**
     * 전체 포인트 사용/적립 내역을 조회합니다.
     *
     * @param pointCode 사용/적립 구분
     * @param pageable  페이지와 사이즈
     * @return 포인트 사용/적립 내역
     * @author 최예린
     * @since 1.0
     */
    Page<PointHistoryResponseDto> getPointHistoriesWithCode(PointCode pointCode, Pageable pageable);

    /**
     * 전체 포인트 내역을 조회합니다.
     *
     * @param pageable 페이지와 사이즈
     * @return 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    Page<PointHistoryResponseDto> getPointHistories(Pageable pageable);
}
