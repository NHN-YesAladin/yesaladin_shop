package shop.yesaladin.shop.wishlist.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.exception.ProductNotFoundException;
import shop.yesaladin.shop.wishlist.domain.model.Wishlist;
import shop.yesaladin.shop.wishlist.domain.repository.CommandWishlistRepository;
import shop.yesaladin.shop.wishlist.dto.WishlistSaveResponseDto;
import shop.yesaladin.shop.wishlist.exception.WishlistAlreadyExistsException;
import shop.yesaladin.shop.wishlist.exception.WishlistNotFoundException;

class CommandWishlistServiceImplTest {

    private CommandWishlistServiceImpl commandWishlistService;
    private CommandWishlistRepository commandWishlistRepository;
    private QueryMemberService queryMemberService;
    private QueryProductRepository queryProductRepository;

    @BeforeEach
    void setUp() {
        commandWishlistRepository = mock(CommandWishlistRepository.class);
        queryMemberService = mock(QueryMemberService.class);
        queryProductRepository = mock(QueryProductRepository.class);
        commandWishlistService = new CommandWishlistServiceImpl(
                commandWishlistRepository,
                queryMemberService,
                queryProductRepository
        );
    }

    @Test
    @DisplayName("save 에서 ProductNotFound 발생")
    void save_ProductNotFound() {
        //then when
        assertThatThrownBy(() -> commandWishlistService.save("loginId", 1L)).isInstanceOf(
                ProductNotFoundException.class);
    }

    @Test
    @DisplayName("save 에서 MemberNotFound 발생")
    void save_MemberNotFound() {
        //given
        Mockito.when(queryProductRepository.findProductById(1L))
                .thenReturn(Optional.of(Product.builder().id(1L).build()));
        Mockito.when(queryMemberService.findByLoginId("loginId")).thenThrow(
                new MemberNotFoundException("Member Login Id: loginId"));

        //when then
        assertThatThrownBy(() -> commandWishlistService.save("loginId", 1L)).isInstanceOf(
                MemberNotFoundException.class);
    }

    @Test
    @DisplayName("save 에서 WishlistAlreadyExistsException")
    void save_WishlistAlreadyExistsException() {
        //given
        Mockito.when(queryProductRepository.findProductById(1L))
                .thenReturn(Optional.of(Product.builder().id(1L).build()));
        Mockito.when(queryMemberService.findByLoginId("loginId"))
                .thenReturn(Member.builder().id(1L).build());
        Mockito.when(commandWishlistRepository.existsByMemberIdAndProductId(1L, 1L))
                .thenReturn(true);

        //when then
        assertThatThrownBy(() -> commandWishlistService.save("loginId", 1L)).isInstanceOf(
                WishlistAlreadyExistsException.class);
    }

    @Test
    @DisplayName("성공")
    void save_success() {
        //given
        LocalDateTime localDateTime = LocalDateTime.now();
        Mockito.when(queryProductRepository.findProductById(1L))
                .thenReturn(Optional.of(Product.builder().id(1L).build()));
        Mockito.when(queryMemberService.findByLoginId("loginId"))
                .thenReturn(Member.builder().id(1L).build());
        Mockito.when(commandWishlistRepository.existsByMemberIdAndProductId(1L, 1L))
                .thenReturn(false);
        Mockito.when(commandWishlistRepository.save(any()))
                .thenReturn(Wishlist.builder()
                        .product(Product.builder().id(1L).build())
                        .registeredDateTime(localDateTime)
                        .build());

        WishlistSaveResponseDto dto = commandWishlistService.save("loginId", 1L);
        assertThat(dto.getProductId()).isEqualTo(1L);
        assertThat(dto.getRegisteredDateTime()).isEqualTo(localDateTime);
    }

    @Test
    @DisplayName("delete에서 MemberNotFound 발생")
    void delete_MemberNotFound() {
        Mockito.when(queryMemberService.findByLoginId("loginId")).thenThrow(
                new MemberNotFoundException("Member Login Id: loginId"));

        assertThatThrownBy(() -> commandWishlistService.delete("loginId", 1L)).isInstanceOf(
                MemberNotFoundException.class);
    }

    @Test
    @DisplayName("delete에서 WishlistAlreadyExistsException 발생")
    void delete_WishlistAlreadyExistsException() {
        Mockito.when(queryMemberService.findByLoginId("loginId"))
                .thenReturn(Member.builder().id(1L).build());
        Mockito.when(commandWishlistRepository.existsByMemberIdAndProductId(1L, 1L))
                .thenReturn(false);
        assertThatThrownBy(() -> commandWishlistService.delete("loginId", 1L)).isInstanceOf(
                WishlistNotFoundException.class);
    }

    @Test
    @DisplayName("delete 성공")
    void delete_success() {
        Mockito.when(queryMemberService.findByLoginId("loginId"))
                .thenReturn(Member.builder().id(1L).build());
        Mockito.when(commandWishlistRepository.existsByMemberIdAndProductId(1L, 1L))
                .thenReturn(true);
        commandWishlistService.delete("loginId", 1L);
        verify(commandWishlistRepository, atLeastOnce()).deleteByMemberIdAndProductId(1L, 1L);
    }

    @Test
    @DisplayName("isExists 에서 MemberNotFound 발생")
    void isExists_MemberNotFound() {
        Mockito.when(queryMemberService.findByLoginId("loginId"))
                .thenThrow(new MemberNotFoundException("loginId"));
        assertThatThrownBy(() -> commandWishlistService.isExists("loginId", 1L)).isInstanceOf(
                MemberNotFoundException.class);
    }

    @Test
    @DisplayName("isExists 성공")
    void isExists_success() {
        Mockito.when(queryMemberService.findByLoginId("loginId"))
                .thenReturn(Member.builder().id(1L).build());
        Mockito.when(commandWishlistRepository.existsByMemberIdAndProductId(1L, 1L))
                .thenReturn(true);

        Boolean exists = commandWishlistService.isExists("loginId", 1L);
        assertThat(exists).isTrue();
    }
}