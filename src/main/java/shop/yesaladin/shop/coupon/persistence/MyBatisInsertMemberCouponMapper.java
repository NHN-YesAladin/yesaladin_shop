package shop.yesaladin.shop.coupon.persistence;

import org.apache.ibatis.annotations.Mapper;
import shop.yesaladin.shop.coupon.domain.repository.InsertMemberCouponRepository;

/**
 * MyBatis 를 사용하여 회원 쿠폰 테이블에 데이터를 삽입하기 위한 Mapper 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Mapper
public interface MyBatisInsertMemberCouponMapper extends InsertMemberCouponRepository {

}
