package shop.yesaladin.shop.wishlist.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.wishlist.domain.model.Wishlist;
import shop.yesaladin.shop.wishlist.domain.repository.QueryWishlistRepository;
import shop.yesaladin.shop.wishlist.dto.WishlistResponseDto;
import shop.yesaladin.shop.wishlist.service.inter.QueryDslWishlistService;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

/**
 * 위시리스트 조회 서비스 구현체
 *
 * @author 김선홍
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryDslWishlistServiceImpl implements QueryDslWishlistService {

    private static final float PERCENT_DENOMINATOR_VALUE = 100;
    private static final long ROUND_OFF_VALUE = 10;
    private final QueryWishlistRepository queryWishlistRepository;
    private final QueryMemberService queryMemberService;
    private final QueryPublishService queryPublishService;
    private final QueryWritingService queryWritingService;

    /**
     *{@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WishlistResponseDto> findWishlistByMemberId(String loginId, Pageable pageable) {
        Member member = queryMemberService.findByLoginId(loginId);

        Page<Wishlist> wishlists = queryWishlistRepository.findWishlistByMemberId(
                member.getId(),
                pageable
        );
        List<WishlistResponseDto> list = new ArrayList<>();
        for (Wishlist wishlist : wishlists) {
            int rate = wishlist.getProduct().getTotalDiscountRate().getDiscountRate();
            if (wishlist.getProduct().isSeparatelyDiscount()) {
                rate = wishlist.getProduct().getDiscountRate();
            }
            List<AuthorsResponseDto> author = findAuthorsByProduct(wishlist.getProduct());
            String publisher = queryPublishService.findByProduct(wishlist.getProduct())
                    .getPublisher()
                    .getName();
            list.add(WishlistResponseDto.fromEntity(
                    wishlist.getProduct(),
                    publisher,
                    author.stream().map(AuthorsResponseDto::getName).collect(Collectors.toList()),
                    wishlist.getRegisteredDateTime(),
                    calcSellingPrice(wishlist.getProduct().getActualPrice(), rate),
                    rate
            ));
        }

        return new PageImpl<>(list, pageable, wishlists.getTotalElements());
    }

    private long calcSellingPrice(long actualPrice, int rate) {
        if (rate > 0) {
            return Math.round((actualPrice - actualPrice * rate / PERCENT_DENOMINATOR_VALUE)
                    / ROUND_OFF_VALUE) * ROUND_OFF_VALUE;
        }
        return actualPrice;
    }

    private List<AuthorsResponseDto> findAuthorsByProduct(Product product) {
        return queryWritingService.findByProduct(product).stream()
                .map(AuthorsResponseDto::getAuthorFromWriting)
                .collect(Collectors.toList());
    }
}
