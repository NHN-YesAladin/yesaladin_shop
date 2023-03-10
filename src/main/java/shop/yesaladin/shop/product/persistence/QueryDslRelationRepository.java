package shop.yesaladin.shop.product.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.product.domain.model.Relation;
import shop.yesaladin.shop.product.domain.model.querydsl.QRelation;
import shop.yesaladin.shop.product.domain.repository.QueryRelationRepository;

/**
 * 상품 연관관계 조회를 위한 Repository QueryDsl 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslRelationRepository implements QueryRelationRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByPk(Relation.Pk pk) {
        QRelation relation = QRelation.relation;

        return queryFactory.select(relation)
                .from(relation)
                .where(relation.pk.eq(pk))
                .fetchFirst() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Relation> findAllForManager(Long productId, Pageable pageable) {
        QRelation relation = QRelation.relation;

        List<Relation> relations = queryFactory.select(relation)
                .from(relation)
                .where(relation.productMain.id.eq(productId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(relation.count())
                .from(relation)
                .where(relation.productMain.id.eq(productId));

        return PageableExecutionUtils.getPage(relations, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Relation> findAll(Long productId, Pageable pageable) {
        QRelation relation = QRelation.relation;

        List<Relation> relations = queryFactory.select(relation)
                .from(relation)
                .where(relation.productMain.id.eq(productId)
                        .and(relation.productSub.isSale.isTrue())
                        .and(relation.productSub.isDeleted.isFalse()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(relation.count())
                .where(relation.productMain.id.eq(productId)
                        .and(relation.productSub.isSale.isTrue())
                        .and(relation.productSub.isDeleted.isFalse()))
                .from(relation);

        return PageableExecutionUtils.getPage(relations, pageable, countQuery::fetchFirst);
    }

}
