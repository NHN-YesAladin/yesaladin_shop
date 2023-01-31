package shop.yesaladin.shop.writing.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.model.querydsl.QAuthor;
import shop.yesaladin.shop.writing.domain.repository.QueryAuthorRepository;

import java.util.List;
import java.util.Optional;

/**
 * 저자 조회를 위한 Repository QueryDsl 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslAuthorRepository implements QueryAuthorRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Author> findById(Long id) {
        QAuthor author = QAuthor.author;

        return Optional.ofNullable(
                queryFactory.select(author)
                        .from(author)
                        .where(author.id.eq(id))
                        .fetchFirst()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Author> findAll() {
        QAuthor author = QAuthor.author;

        return queryFactory.select(author)
                .from(author)
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Author> findAllForManager(Pageable pageable) {
        QAuthor author = QAuthor.author;

        List<Author> authors = queryFactory.select(author)
                .from(author)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(author.count())
                .from(author)
                .fetchFirst();

        return new PageImpl<>(authors, pageable, totalCount);
    }
}
