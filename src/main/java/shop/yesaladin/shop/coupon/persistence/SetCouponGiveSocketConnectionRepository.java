package shop.yesaladin.shop.coupon.persistence;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.coupon.domain.repository.CouponGiveSocketConnectionRepository;

/**
 * CouponGiveSocketConnectionRepository 인터페이스의 SynchronizedSet을 사용한 구현체입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Repository
public class SetCouponGiveSocketConnectionRepository implements
        CouponGiveSocketConnectionRepository {
    private static final Set<String> requestIdSet = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void save(String requestId) {
        requestIdSet.add(requestId);
    }

    @Override
    public boolean existsByRequestId(String requestId) {
        return requestIdSet.contains(requestId);
    }

    @Override
    public void delete(String requestId) {
        requestIdSet.remove(requestId);
    }
}
