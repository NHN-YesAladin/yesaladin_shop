package shop.yesaladin.shop.order.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.order.domain.model.querydsl.QOrderProduct;
import shop.yesaladin.shop.order.domain.repository.QueryOrderProductRepository;

/**
 * 주문 상품 데이터 조회를 위한 레포지토리의 QueryDsl 구현체입니다.
 *
 * @author 배수한
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslOrderProductQueryRepository implements QueryOrderProductRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Long getCountOfOrderProductByOrderId(Long orderId) {
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        return queryFactory.select(orderProduct.count())
                .from(orderProduct)
                .where(orderProduct.order.id.eq(orderId))
                .fetchFirst();
    }
}
