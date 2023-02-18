package shop.yesaladin.shop.order.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import shop.yesaladin.shop.coupon.domain.repository.InsertMemberCouponRepository;
import shop.yesaladin.shop.order.domain.repository.QuerySalesStatisticsRepository;
import shop.yesaladin.shop.order.dto.SalesStatisticsMyBatisResponseDto;

import java.util.List;

/**
 * 매출 통계 정보를 조회하기 위한 MyBatis Mapper 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Mapper
public interface MyBatisSalesStatisticsMapper extends QuerySalesStatisticsRepository {

    /**
     * 지난 1년동안 가장 매출이 좋은 12개의 상품을 조회합니다.
     *
     * @return 조회된 베스트셀러
     * @author 이수정
     * @since 1.0
     */
    List<Long> getBestseller(@Param("start") String start, @Param("end") String end);
}
