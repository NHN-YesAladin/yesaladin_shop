package shop.yesaladin.shop.point.domain.repository;

import shop.yesaladin.shop.point.domain.model.PointHistory;

/**
 * 포인트내역 등록/수정/삭제 관련 repository interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandPointHistoryRepository {

    /**
     * 포인트 내역을 등록합니다.
     *
     * @param pointHistory 등록할 포인트 내역
     * @return 등록된 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    PointHistory save(PointHistory pointHistory);
}
