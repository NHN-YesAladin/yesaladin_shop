package shop.yesaladin.shop.payment.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.repository.QueryPaymentRepository;

/**
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
        return Optional.empty();
    }

    @Override
    public Optional<Payment> findById(Long orderId) {
        return Optional.empty();
    }
}
