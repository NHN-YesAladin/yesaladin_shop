package shop.yesaladin.shop.wishlist.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.wishlist.domain.model.Wishlist;
import shop.yesaladin.shop.wishlist.domain.model.Wishlist.Pk;
import shop.yesaladin.shop.wishlist.domain.repository.CommandWishlistRepository;

/**
 * 위시리스트를 등록, 삭제할 레포지토 구현체
 *
 * @author 김선홍
 * @since 1.0
 */
public interface JpaCommandWishlistRepository extends Repository<Wishlist, Pk>,
        CommandWishlistRepository {

}
