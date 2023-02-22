package shop.yesaladin.shop.order.domain.repository;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import shop.yesaladin.shop.order.dto.SalesStatisticsMyBatisResponseDto;

/**
 * 매출 통계 정보를 조회하기 위한 MyBatis Repository 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QuerySalesStatisticsRepository {

    /**
     * 기간에 따른 매출 통계를 Paging 하여 조회합니다.
     *
     * @param start  시작일
     * @param end    종료일
     * @param limit
     * @param offset
     * @return Paging 조회된 매출 통계
     * @author 이수정
     * @since 1.0
     */
    List<SalesStatisticsMyBatisResponseDto> getSalesStatistics(
            @Param("start") String start,
            @Param("end") String end,
            @Param("limit") int limit,
            @Param("offset") long offset
    );

    /**
     * 기간에 따른 매출 통계의 총 데이터 개수를 조회합니다.
     *
     * @param start 시작일
     * @param end   종료일
     * @return 조회된 매출 통계의 총 데이터 개수
     * @author 이수정
     * @since 1.0
     */
    Integer getSalesStatisticsTotalCount(@Param("start") String start, @Param("end") String end);

    /**
     * 지난 1년동안 가장 매출이 좋은 12개의 상품을 조회합니다.
     *
     * @return 조회된 베스트셀러
     * @author 이수정
     * @since 1.0
     */
    List<Long> getBestseller(@Param("start") String start, @Param("end") String end);
}
