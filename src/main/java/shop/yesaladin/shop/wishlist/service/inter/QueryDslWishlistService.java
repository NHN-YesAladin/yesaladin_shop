package shop.yesaladin.shop.wishlist.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.wishlist.dto.WishlistResponseDto;

/**
 * 위시리스트 조회 서비스 인터페이스
 *
 * @author 김선홍
 * @since 1.0
 */
public interface QueryDslWishlistService {

    /**
     * 해당 유저의 위시리스트 조회
     *
     * @param loginId 조회할 회원의 id
     * @param pageable 페이지 정보
     * @author 김선홍
     * @since 1.0
     */
    Page<WishlistResponseDto> findWishlistByMemberId(String loginId, Pageable pageable);
}
