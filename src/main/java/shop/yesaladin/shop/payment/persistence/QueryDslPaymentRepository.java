package shop.yesaladin.shop.payment.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.category.domain.model.querydsl.QCategory;
import shop.yesaladin.shop.order.domain.model.querydsl.QOrder;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.querydsl.QPayment;
import shop.yesaladin.shop.payment.domain.model.querydsl.QPaymentCancel;
import shop.yesaladin.shop.payment.domain.model.querydsl.QPaymentCard;
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
    public Optional<Payment> findById(String id, Long orderId) {
        QPayment payment = QPayment.payment;
        QPaymentCard paymentCard = QPaymentCard.paymentCard;
        QPaymentCancel paymentCancel = QPaymentCancel.paymentCancel;
        QOrder order = QOrder.order;

        return Optional.ofNullable(queryFactory.selectFrom(payment)
                .innerJoin(payment.order, order)
                .fetchJoin()
                .innerJoin(payment.paymentCard, paymentCard)
                .fetchJoin()
                .leftJoin(payment.paymentCancel, paymentCancel)
                .fetchJoin()
                .where(paymentIdEq(payment, id), orderIdEq(payment, orderId))
                .fetchFirst());
    }

    /**
     * 동적 쿼리를 위한 메서드
     *  payment의 id가 null이 아니면 where 절에서 적용
     *
     * @param payment Q객체
     * @param paymentId 찾고자하는 결제 정보 id
     * @return BooleanExpression where 절에서 사용
     */
    private BooleanExpression paymentIdEq(QPayment payment, String paymentId) {
        if (Objects.isNull(paymentId)) {
            return null;
        }
        return payment.id.eq(paymentId);
    }

    /**
     * 동적 쿼리를 위한 메서드
     *  payment와 연관관계가 있는 주문의 id가 null이 아니면 where 절에서 적용
     *
     * @param payment Q객체
     * @param orderId 찾고자하는 결제 정보와 연관관계가 있는 order id
     * @return BooleanExpression where 절에서 사용
     */
    private BooleanExpression orderIdEq(QPayment payment, Long orderId) {
        if (Objects.isNull(orderId)) {
            return null;
        }
        return payment.order.id.eq(orderId);
    }

}
