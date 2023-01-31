package shop.yesaladin.shop.point.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;

/**
 * 포인트 내역 조회 관련 repository 인터페이스 입니다.
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
     * @param loginId   회원 아이디
     * @param startDate 조회 첫 날짜
     * @param endDate   조회 마지막 날짜
     * @return 기간별 회원의 포인트 사용/적립 내역
     * @author 최예린
     * @since 1.0
     */
    List<PointHistory> findByLoginId(String loginId, LocalDate startDate, LocalDate endDate);

    /**
     * 포인트 사용/적립에 따른 포인트 내역을 조회합니다.
     *
     * @param pointCode 사용/적립
     * @param startDate 조회 첫 날짜
     * @param endDate   조회 마지막 날짜
     * @return 기간별 사용/적립에 따른 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    List<PointHistory> findByPointCode(PointCode pointCode, LocalDate startDate, LocalDate endDate);

    /**
     * 회원 아이디를 통해 회원이 가진 포인트를 계산합니다.
     *
     * @param loginId 회원의 Id
     * @return 회원의 포인트
     * @author 최예린
     * @since 1.0
     */
    long getMemberPointByLoginId(String loginId);

    /**
     * 회원 아이디와 사용/적립 코드로 페이지별 포인트 내역을 조회합니다.
     *
     * @param loginId   회원 아이디
     * @param pointCode 사용/적립 구분
     * @param pageable  페이지와 사이즈
     * @return 회원의 사용/적립 코드별 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    Page<PointHistoryResponseDto> getByLoginIdAndPointCode(
            String loginId,
            PointCode pointCode,
            Pageable pageable
    );

    /**
     * 회원 아이디로 페이지별 포인트 내역을 조회합니다.
     *
     * @param loginId  회원 아이디
     * @param pageable 페이지와 사이즈
     * @return 회원의 전체 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    Page<PointHistoryResponseDto> getByLoginId(String loginId, Pageable pageable);

    /**
     * 사용/적립 코드로 페이지별 포인트 내역을 조회합니다.
     *
     * @param pointCode 사용/적립 구분
     * @param pageable  페이지와 사이즈
     * @return 사용/적립 코드별 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    Page<PointHistoryResponseDto> getByPointCode(PointCode pointCode, Pageable pageable);

    /**
     * 페이지별 포인트 내역을 조회합니다.
     *
     * @param pageable 페이지와 사이즈
     * @return 페이지별 전체 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    Page<PointHistoryResponseDto> getBy(Pageable pageable);
}
