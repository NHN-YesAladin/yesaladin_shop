package shop.yesaladin.shop.publish.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
@Repository
@RequiredArgsConstructor
public class QueryDslPublisherRepository implements QueryPublisherRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * Id를 기준으로 출판사를 조회합니다.
     *
     * @param id 출판사의 Id (PK)
     * @return 조회된 출판사 엔터티
     * @author 이수정
     * @since 1.0
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
     * 출판사를 전체 조회합니다.
     *
     * @return 조회된 출판사 엔터티 List
     * @author 이수정
     * @since 1.0
     */
    @Override
    public List<Publisher> findAll() {
        QPublisher publisher = QPublisher.publisher;

        return queryFactory.select(publisher)
                .from(publisher)
                .fetch();
    }

    /**
     * 출판사를 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 출판사 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Page<Publisher> findAllForManager(Pageable pageable) {
        QPublisher publisher = QPublisher.publisher;

        List<Publisher> publishers = queryFactory.select(publisher)
                .from(publisher)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(publisher.count())
                .from(publisher)
                .fetchFirst();

        return new PageImpl<>(publishers, pageable, totalCount);
    }

    /**
     * 이미 존재하는 출판사 이름인지 확인합니다.
     *
     * @return 확인할 출판사 이름
     * @author 이수정
     * @since 1.0
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
