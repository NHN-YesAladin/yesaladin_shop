package shop.yesaladin.shop.wishlist.domain.repository;

import shop.yesaladin.shop.wishlist.domain.model.Wishlist;

/**
 * 위시리스트 추가, 삭제하는 레포지터리 인터페이스
 *
 * @author 김선홍
 * @since 1.0
 */
public interface CommandWishlistRepository {

    /**
     * 위시리스트 등록 메서드
     *
     * @param wishlist 등록할 위시리스트
     * @author 김선홍
     * @since 1.0
     */
    Wishlist save(Wishlist wishlist);

    /**
     * 위시리스트 삭제 메서드
     *
     * @param memberId 위시리스트에 물품을 삭제할 유저의 id
     * @param productId 위시리스트에 삭제될 상품의 id
     * @author 김선홍
     * @since 1.0
     */
    void deleteByMemberIdAndProductId(Long memberId, Long productId);

    Boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
