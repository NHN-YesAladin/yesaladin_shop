package shop.yesaladin.shop.order.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.order.domain.model.querydsl.QOrder;
import shop.yesaladin.shop.order.domain.model.querydsl.QOrderProduct;
import shop.yesaladin.shop.order.domain.repository.QueryOrderProductRepository;
import shop.yesaladin.shop.order.dto.OrderProductResponseDto;
import shop.yesaladin.shop.product.domain.model.querydsl.QProduct;
import shop.yesaladin.shop.product.dto.ProductOrderQueryResponseDto;

import java.util.List;

/**
 * 주문 상품 데이터 조회를 위한 레포지토리의 QueryDsl 구현체입니다.
 *
 * @author 배수한
 * @since 1.0
 */

@RequiredArgsConstructor
@Repository
public class QueryDslOrderProductRepository implements QueryOrderProductRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrderProductResponseDto> findAllByOrderNumber(String orderNumber) {
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        QOrder order = QOrder.order;
        QProduct product = QProduct.product;
        return queryFactory.select(Projections.constructor(
                        OrderProductResponseDto.class,
                        Projections.constructor(
                                ProductOrderQueryResponseDto.class,
                                orderProduct.product.id,
                                orderProduct.product.isbn,
                                orderProduct.product.title,
                                orderProduct.product.actualPrice,
                                orderProduct.product.discountRate,
                                orderProduct.product.isGivenPoint,
                                orderProduct.product.givenPointRate,
                                orderProduct.product.quantity
                        ),
                        orderProduct.quantity
                ))
                .from(orderProduct)
                .innerJoin(orderProduct.order, order)
                .innerJoin(orderProduct.product, product)
                .where(orderProduct.order.orderNumber.eq(orderNumber))
                .where(orderProduct.isCanceled.isFalse())
                .orderBy(orderProduct.quantity.desc())
                .fetch();
    }
}
