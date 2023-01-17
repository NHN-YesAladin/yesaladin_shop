package shop.yesaladin.shop.point.domain.repository;

import java.util.Optional;
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
     * 회원 아이디를 통해 회원이 가진 포인트를 계산합니다.
     *
     * @param memberId 회원의 Id
     * @return 회원의 포인트
     * @author 최예린
     * @since 1.0
     */
    long getMemberPointByMemberId(long memberId);

}
