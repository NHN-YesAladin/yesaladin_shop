package shop.yesaladin.shop.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.wishlist.domain.model.Wishlist;
import shop.yesaladin.shop.wishlist.domain.repository.CommandWishlistRepository;
import shop.yesaladin.shop.wishlist.dto.WishlistSaveResponseDto;
import shop.yesaladin.shop.wishlist.service.inter.CommandWishlistService;
import shop.yesaladin.shop.wishlist.service.inter.QueryWishlistService;

/**
 * 위시리스트 등록 및 삭제 서비스 구현체
 *
 * @author 김선홍
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandWishlistServiceImpl implements CommandWishlistService {

    private final CommandWishlistRepository commandWishlistRepository;
    private final QueryMemberService queryMemberService;
    private final QueryProductRepository queryProductRepository;
    private final QueryWishlistService queryWishlistService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public WishlistSaveResponseDto save(String loginId, Long productId) {
        Product product = queryProductRepository.findProductById(productId).orElseThrow(() -> {
            throw new ClientException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    "Product is not found: " + productId
            );
        });
        Member member = queryMemberService.findByLoginId(loginId);
        if (Boolean.TRUE.equals(queryWishlistService.isExists(
                member.getLoginId(),
                productId
        ))) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "Wishlist is exists: " + productId);
        }
        Wishlist wishlist = commandWishlistRepository.save(Wishlist.create(
                member,
                product
        ));
        return new WishlistSaveResponseDto(productId, wishlist.getRegisteredDateTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(String loginId, Long productId) {
        Member member = queryMemberService.findByLoginId(loginId);
        if (Boolean.FALSE.equals(queryWishlistService.isExists(
                member.getLoginId(),
                productId
        ))) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Wishlist is not exists: " + productId
            );
        }
        commandWishlistRepository.deleteByMemberIdAndProductId(member.getId(), productId);
    }
}
