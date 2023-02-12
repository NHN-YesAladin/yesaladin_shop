package shop.yesaladin.shop.wishlist.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.publish.domain.model.Publish.Pk;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.exception.PublisherNotFoundException;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.wishlist.domain.model.Wishlist;
import shop.yesaladin.shop.wishlist.domain.repository.QueryWishlistRepository;
import shop.yesaladin.shop.wishlist.dto.WishlistResponseDto;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

class QueryWishlistServiceImplTest {

    private QueryWishlistServiceImpl queryDslWishlistService;
    private QueryWishlistRepository queryWishlistRepository;
    private QueryMemberService queryMemberService;
    private QueryPublishService queryPublishService;
    private QueryWritingService queryWritingService;
    private Product product;
    private Member member;

    @BeforeEach
    void setUp() {
        queryWishlistRepository = Mockito.mock(QueryWishlistRepository.class);
        queryPublishService = Mockito.mock(QueryPublishService.class);
        queryMemberService = Mockito.mock(QueryMemberService.class);
        queryWritingService = Mockito.mock(QueryWritingService.class);
        queryDslWishlistService = new QueryWishlistServiceImpl(
                queryWishlistRepository,
                queryMemberService,
                queryPublishService,
                queryWritingService
        );

        product = Product.builder()
                .id(1L)
                .isSeparatelyDiscount(true)
                .discountRate(10)
                .thumbnailFile(File.builder().url("url").build())
                .totalDiscountRate(TotalDiscountRate.builder().discountRate(10).build())
                .build();

        member = Member.builder().id(1L).build();
    }

    @Test
    @DisplayName("findWishlistByMemberId에서 MemberNotFound 발생")
    void findWishlistByMemberId_MemberNotFound() {
        //given
        Mockito.when(queryMemberService.findByLoginId("loginId")).thenThrow(
                new MemberNotFoundException("Member Login Id: loginId"));

        //when then
        assertThatThrownBy(() -> queryDslWishlistService.findWishlistByMemberId(
                "loginId",
                PageRequest.of(0, 10)
        )).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("findWishlistByMemberId에서 PublisherNotFound 발생")
    void findWishlistByMemberId_PublisherNotFound() {
        //given
        Mockito.when(queryMemberService.findByLoginId("loginId")).thenReturn(member);
        Mockito.when(queryPublishService.findByProduct(product))
                .thenThrow(new PublisherNotFoundException(product.getId()));
        Mockito.when(queryWishlistRepository.findWishlistByMemberId(1L, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(
                        List.of(Wishlist.create(member, product)),
                        PageRequest.of(0, 10),
                        1L
                ));

        //when then
        assertThatThrownBy(() -> queryDslWishlistService.findWishlistByMemberId(
                "loginId",
                PageRequest.of(0, 10)
        )).isInstanceOf(PublisherNotFoundException.class);
    }

    @Test
    @DisplayName("findWishlistByMemberId 성공")
    void findWishlistByMemberId_save() {
        //given
        Member member = Member.builder().id(1L).build();
        Author author = Author.builder().id(1L).name("author").build();

        Publisher publisher = Publisher.builder().id(1L).name("publisher").build();

        LocalDate publishedDate = LocalDate.now();

        Mockito.when(queryMemberService.findByLoginId("loginId"))
                .thenReturn(member);

        Mockito.when(queryWishlistRepository.findWishlistByMemberId(1L, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(Wishlist.create(member, product))));

        Mockito.when(queryWritingService.findByProduct(product)).thenReturn(List.of(
                new WritingResponseDto(product, author)));

        Mockito.when(queryPublishService.findByProduct(product))
                .thenReturn(new PublishResponseDto(Pk.builder()
                        .productId(1L)
                        .publisherId(1L)
                        .build(), publishedDate, product, publisher));
        //when
        Page<WishlistResponseDto> dtoPage = queryDslWishlistService.findWishlistByMemberId(
                "loginId",
                PageRequest.of(0, 10)
        );
        //then
        assertThat(dtoPage.getTotalElements()).isEqualTo(1L);
        assertThat(dtoPage.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findWishlistByMemberId 성공")
    void findWishlistByMemberId_save_rate_less_than_0() {
        Author author = Author.builder().id(1L).name("author").build();

        Publisher publisher = Publisher.builder().id(1L).name("publisher").build();

        LocalDate publishedDate = LocalDate.now();

        Mockito.when(queryMemberService.findByLoginId("loginId"))
                .thenReturn(member);

        Mockito.when(queryWishlistRepository.findWishlistByMemberId(1L, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(Wishlist.create(member, product))));

        Mockito.when(queryWritingService.findByProduct(product)).thenReturn(List.of(
                new WritingResponseDto(product, author)));

        Mockito.when(queryPublishService.findByProduct(product))
                .thenReturn(new PublishResponseDto(Pk.builder()
                        .productId(1L)
                        .publisherId(1L)
                        .build(), publishedDate, product, publisher));
        //when
        Page<WishlistResponseDto> dtoPage = queryDslWishlistService.findWishlistByMemberId(
                "loginId",
                PageRequest.of(0, 10)
        );
        //then
        assertThat(dtoPage.getTotalElements()).isEqualTo(1L);
        assertThat(dtoPage.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("isExists에서 MemberNotFound 발생")
    void isExists_MemberNotFound() {
        //given
        Mockito.when(queryMemberService.findByLoginId("loginId")).thenThrow(
                new MemberNotFoundException("Member Login Id: loginId"));

        //when then
        assertThatThrownBy(() -> queryDslWishlistService.isExists(
                "loginId",
                1L
        )).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("isExists에서 성공")
    void isExists_success() {
        Mockito.when(queryMemberService.findByLoginId("loginId"))
                .thenReturn(member);
        Mockito.when(queryWishlistRepository.existsByMemberIdAndProductId(any(), eq(1L)))
                .thenReturn(true);

        Boolean result = queryDslWishlistService.isExists("loginId", 1L);
        assertThat(result).isTrue();
    }
}