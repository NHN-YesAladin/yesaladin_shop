package shop.yesaladin.shop.wishlist.service.inter;

/**
 * 위시리스트 등록 및 삭제 서비스 인터페이스
 *
 * @author 김선홍
 * @since 1.0
 */
public interface CommandWishlistService {

    /**
     * 위시리스트 등록 메소드
     *
     * @param memberId 위시리스트의 유저의 id
     * @param productId 위시리스트에 등록할 상품의 id
     */
    void save(Long memberId, Long productId);

    /**
     * 위시리스트 삭제 메소드
     *
     * @param memberId 위시리스트의 유저의 id
     * @param productId 위시리스트에 삭제할 상품의 id
     */
    void delete(Long memberId, Long productId);
}
