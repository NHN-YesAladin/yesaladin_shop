package shop.yesaladin.shop.payment.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.repository.QueryPaymentRepository;

/**
 * QueryDsl을 활용하여 결제 정보를 조회할때 사용
 *   결제 취소 정보, 결제 카드 정보는 결제 정보를 조회 할 때 함께 조회가 되어야하기 때문에 대부분 join 되어있다.
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Repository
public class QueryDslPaymentRepository implements QueryPaymentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Payment> findById(String id) {
        queryFactory.select()
        return Optional.empty();
    }

    @Override
    public Optional<Payment> findById(Long orderId) {
        return Optional.empty();
    }
}
