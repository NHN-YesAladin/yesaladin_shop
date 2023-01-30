package shop.yesaladin.shop.publish.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.model.querydsl.QPublisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;

import java.util.List;
import java.util.Optional;

/**
 * 출판사 조회를 위한 Repository QueryDsl 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslPublisherRepository implements QueryPublisherRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Publisher> findById(Long id) {
        QPublisher publisher = QPublisher.publisher;

        return Optional.ofNullable(
                queryFactory.select(publisher)
                        .from(publisher)
                        .where(publisher.id.eq(id))
                        .fetchFirst()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Publisher> findAll() {
        QPublisher publisher = QPublisher.publisher;

        return queryFactory.select(publisher)
                .from(publisher)
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Publisher> findAllForManager(Pageable pageable) {
        QPublisher publisher = QPublisher.publisher;

        List<Publisher> publishers = queryFactory.select(publisher)
                .from(publisher)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(publisher.count()).from(publisher);

        return PageableExecutionUtils.getPage(publishers, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByName(String name) {
        QPublisher publisher = QPublisher.publisher;

        return queryFactory.select(publisher)
                .from(publisher)
                .where(publisher.name.eq(name))
                .fetchFirst() != null;
    }
}
