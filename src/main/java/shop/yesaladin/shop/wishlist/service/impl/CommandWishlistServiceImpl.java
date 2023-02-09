package shop.yesaladin.shop.wishlist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.exception.ProductNotFoundException;
import shop.yesaladin.shop.wishlist.domain.model.Wishlist;
import shop.yesaladin.shop.wishlist.domain.repository.CommandWishlistRepository;
import shop.yesaladin.shop.wishlist.dto.WishlistSaveResponseDto;
import shop.yesaladin.shop.wishlist.exception.WishlistAlreadyExistsException;
import shop.yesaladin.shop.wishlist.exception.WishlistNotFoundException;
import shop.yesaladin.shop.wishlist.service.inter.CommandWishlistService;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public WishlistSaveResponseDto save(String loginId, Long productId) {
        Product product = queryProductRepository.findProductById(productId).orElseThrow(() -> {
            throw new ProductNotFoundException(productId);
        });
        Member member = queryMemberService.findByLoginId(loginId);
        if(commandWishlistRepository.existsByMemberIdAndProductId(member.getId(), productId)) {
            throw new WishlistAlreadyExistsException(loginId, productId);
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
    public void delete(String loginId, Long productId) {
        Member member = queryMemberService.findByLoginId(loginId);
        if(!commandWishlistRepository.existsByMemberIdAndProductId(member.getId(), productId)) {
            throw new WishlistNotFoundException(loginId, productId);
        }
        commandWishlistRepository.deleteByMemberIdAndProductId(member.getId(), productId);
    }
}
