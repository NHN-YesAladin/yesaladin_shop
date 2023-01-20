package shop.yesaladin.shop.tag.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandProductTagRepository;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandProductTagServiceImplTest {

    private final String ISBN = "00001-...";
    private final String TAG_NAME = "눈물나는";

    private CommandProductTagService commandProductTagService;
    private CommandProductTagRepository commandProductTagRepository;

    private ProductTag productTag;

    @BeforeEach
    void setUp() {
        Product product = DummyProduct.dummy(ISBN);
        Tag tag = Tag.builder().name(TAG_NAME).build();

        productTag = ProductTag.create(product, tag);

        commandProductTagRepository = mock(CommandProductTagRepository.class);
        commandProductTagService = new CommandProductTagServiceImpl(commandProductTagRepository);
    }

    @Test
    void register() {
        // given
        when(commandProductTagRepository.save(any())).thenReturn(productTag);

        // when
        ProductTagResponseDto registeredProductTag = commandProductTagService.register(productTag);

        // then
        assertThat(registeredProductTag.getProduct().getISBN()).isEqualTo(ISBN);
        assertThat(registeredProductTag.getTag().getName()).isEqualTo(TAG_NAME);
    }
}
