package shop.yesaladin.shop.order.persistence;

import org.apache.ibatis.annotations.Mapper;
import shop.yesaladin.shop.order.domain.repository.QuerySalesStatisticsRepository;

/**
 * 매출 통계 정보를 조회하기 위한 MyBatis Mapper 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Mapper
public interface MyBatisSalesStatisticsMapper extends QuerySalesStatisticsRepository {

}
