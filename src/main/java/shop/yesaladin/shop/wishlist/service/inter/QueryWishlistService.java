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
public interface QueryWishlistService {

    /**
     * 해당 유저의 위시리스트 조회
     *
     * @param loginId  조회할 회원의 id
     * @param pageable 페이지 정보
     * @author 김선홍
     * @since 1.0
     */
    Page<WishlistResponseDto> findWishlistByMemberId(String loginId, Pageable pageable);

    /**
     * 위시리스트 등록 유무 확인 등록 되어 있다면 true or false
     *
     * @param loginId   확인할 유저의 loginId
     * @param productId 상품 Id
     * @return 등록되어 있다면 true or false
     */
    Boolean isExists(String loginId, Long productId);
}
