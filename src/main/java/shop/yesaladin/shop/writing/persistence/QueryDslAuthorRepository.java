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
@Repository
@RequiredArgsConstructor
public class QueryDslAuthorRepository implements QueryAuthorRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * Id를 기준으로 저자를 조회합니다.
     *
     * @param id 저자의 Id (PK)
     * @return 조회된 저자 엔터티
     * @author 이수정
     * @since 1.0
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
     * 저자를 전체 조회합니다.
     *
     * @return 조회된 저자 엔터티 List
     * @author 이수정
     * @since 1.0
     */
    @Override
    public List<Author> findAll() {
        QAuthor author = QAuthor.author;

        return queryFactory.select(author)
                .from(author)
                .fetch();
    }

    /**
     * 저자를 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 저자 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Page<Author> findAllForManager(Pageable pageable) {
        QAuthor author = QAuthor.author;

        List<Author> authors = queryFactory.select(author)
                .from(author)
                .fetch();

        return new PageImpl<>(authors, pageable, authors.size());
    }
}
