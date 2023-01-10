package shop.yesaladin.shop.writing.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.domain.repository.CommandWritingRepository;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;

class CommandWritingServiceImplTest {

    private final String AUTHOR_NAME = "테스트";

    private Product product;

    private CommandWritingService commandWritingService;
    private CommandWritingRepository commandWritingRepository;


    @BeforeEach
    void setUp() {
        product = DummyProduct.dummy("00001-...");

        commandWritingRepository = mock(CommandWritingRepository.class);
        commandWritingService = new CommandWritingServiceImpl(commandWritingRepository);
    }

    @Test
    void create() {
        // given
        Writing writing = Writing.builder()
                .authorName(AUTHOR_NAME)
                .product(product)
                .member(null)
                .build();

        when(commandWritingRepository.save(any())).thenReturn(writing);

        // when
        WritingResponseDto createdWriting = commandWritingService.create(AUTHOR_NAME, product, null);

        // then
        assertThat(createdWriting.getAuthorName()).isEqualTo(AUTHOR_NAME);
    }
}
