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

}
