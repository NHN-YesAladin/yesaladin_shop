package shop.yesaladin.shop.point.domain.repository;

import java.awt.Point;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;

/**
 * 포인트 내역 조회 관련 repository 클래스 입니다.
 *
 * @author 최에린
 * @since 1.0
 */
public interface QueryPointHistoryRepository {

    /**
     * pk 를 통해 포인트내역을 조회합니다.
     *
     * @param id pk
     * @return 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    Optional<PointHistory> findById(long id);

    /**
     * 회원의 아이디를 통해 회원이 가진 포인트내역을 조회합니다.
     *
     * @param memberId 회원 아이디
     * @param startDate 조회 첫 날짜
     * @param endDate 조회 마지막 날짜
     * @return 기간별 회원의 포인트 사용/적립 내역
     * @author 최예린
     * @since 1.0
     */
    List<PointHistory> findByMemberId(long memberId, LocalDate startDate, LocalDate endDate);

    /**
     * 포인트 사용/적립에 따른 포인트 내역을 조회합니다.
     *
     * @param pointCode 사용/적립
     * @param startDate 조회 첫 날짜
     * @param endDate 조회 마지막 날짜
     * @return 기간별 사용/적립에 따른 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    List<PointHistory> findByPointCode(PointCode pointCode, LocalDate startDate, LocalDate endDate);

    /**
     * 회원 아이디를 통해 회원이 가진 포인트를 계산합니다.
     *
     * @param memberId 회원의 Id
     * @return 회원의 포인트
     * @author 최예린
     * @since 1.0
     */
    long getMemberPointByMemberId(long memberId);

}
