package shop.yesaladin.shop.tag.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@Repository
@RequiredArgsConstructor
public class QueryDslTagRepository implements QueryTagRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * Id를 기준으로 태그를 조회합니다.
     *
     * @param id 태그의 Id (PK)
     * @return 조회된 태그 엔터티
     * @author 이수정
     * @since 1.0
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
     * 태그를 전체 조회합니다.
     *
     * @return 조회된 태그 엔터티 List
     * @author 이수정
     * @since 1.0
     */
    @Override
    public List<Tag> findAll() {
        QTag tag = QTag.tag;

        return queryFactory.select(tag)
                .from(tag)
                .fetch();
    }

    /**
     * 태그를 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 태그 엔터티 Page
     * @author 이수정
     * @since 1.0
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
     * 이미 존재하는 태그 이름인지 확인합니다.
     *
     * @return 확인할 태그 이름
     * @author 이수정
     * @since 1.0
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
