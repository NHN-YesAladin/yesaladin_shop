package shop.yesaladin.shop.wishlist.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.wishlist.domain.model.Wishlist;

/**
 * QueryDsl 위시리스트를 조회하는 레포지터리 인터페이스
 *
 * @author 김선홍
 * @since 1.0
 */
public interface QueryWishlistRepository {

    /**
     * 해당 유저의 위시리스트 조회
     *
     * @param memberId 조회할 회원의 id
     * @param pageable 페이지 정보
     *
     */
    Page<Wishlist> findWishlistByMemberId(Long memberId, Pageable pageable);
}
