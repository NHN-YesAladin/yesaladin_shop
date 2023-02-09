package shop.yesaladin.shop.wishlist.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.wishlist.domain.model.Wishlist;
import shop.yesaladin.shop.wishlist.domain.model.querydsl.QWishlist;
import shop.yesaladin.shop.wishlist.domain.repository.QueryWishlistRepository;

/**
 * QueryDsl 을 이용한 위시리스트 조회 레포지토리 구현체
 *
 * @author 김선홍
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslWishlistRepository implements QueryWishlistRepository {

    private final JPAQueryFactory queryFactory;

    /**
     *{@inheritDoc}
     */
    @Override
    public Page<Wishlist> findWishlistByMemberId(Long memberId, Pageable pageable) {
        QWishlist wishlist = QWishlist.wishlist;

        List<Wishlist> wishlists = queryFactory.select(wishlist)
                .from(wishlist)
                .where(wishlist.pk.memberId.eq(memberId))
                .offset((long) pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageNumber())
                .fetch();
        Long count = queryFactory.select(wishlist.pk.productId)
                .from(wishlist)
                .where(wishlist.pk.memberId.eq(memberId))
                .fetchFirst();

        return new PageImpl<>(wishlists, pageable, count);
    }
}
