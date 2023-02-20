package shop.yesaladin.shop.tag.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.model.querydsl.QTag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;

import java.util.List;
import java.util.Optional;

/**
 * 태그 조회를 위한 Repository QueryDsl 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslTagRepository implements QueryTagRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Tag> findById(Long id) {
        QTag tag = QTag.tag;

        return Optional.ofNullable(
                queryFactory.select(tag)
                        .from(tag)
                        .where(tag.id.eq(id))
                        .fetchFirst()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tag> findAll() {
        QTag tag = QTag.tag;

        return queryFactory.select(tag)
                .from(tag)
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Tag> findAllForManager(Pageable pageable) {
        QTag tag = QTag.tag;

        List<Tag> tags = queryFactory.select(tag)
                .from(tag)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(tag.count()).from(tag);

        return PageableExecutionUtils.getPage(tags, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Tag> findByNameForManager(String name, Pageable pageable) {
        QTag tag = QTag.tag;

        List<Tag> tags = queryFactory.select(tag)
                .from(tag)
                .where(tag.name.contains(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = queryFactory.select(tag.count())
                .from(tag)
                .where(tag.name.contains(name))
                .fetchFirst();
        return new PageImpl<>(tags, pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByName(String name) {
        QTag tag = QTag.tag;

        return queryFactory.select(tag)
                .from(tag)
                .where(tag.name.eq(name))
                .fetchFirst() != null;
    }
}
