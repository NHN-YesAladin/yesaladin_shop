package shop.yesaladin.shop.tag.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandProductTagRepository;
import shop.yesaladin.shop.tag.domain.repository.QueryProductTagRepository;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandProductTagServiceImplTest {

    private final String ISBN = "0000000000001";
    private final String NAME = "행복한";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private CommandProductTagService service;
    private CommandProductTagRepository commandProductTagRepository;
    private QueryProductTagRepository queryProductTagRepository;

    private Product product;
    private Tag tag;
    private ProductTag productTag;

    @BeforeEach
    void setUp() {
        product = DummyProduct.dummy(
                ISBN,
                DummySubscribeProduct.dummy(),
                DummyFile.dummy(URL + "/image.png"),
                DummyFile.dummy(URL + "/ebook.pdf"),
                DummyTotalDiscountRate.dummy()
        );
        tag = Tag.builder().name(NAME).build();

        productTag = ProductTag.create(product, tag);

        commandProductTagRepository = mock(CommandProductTagRepository.class);
        queryProductTagRepository = mock(QueryProductTagRepository.class);
        service = new CommandProductTagServiceImpl(
                commandProductTagRepository,
                queryProductTagRepository
        );
    }

    @Test
    @DisplayName("태그 관계 등록 성공")
    void register() {
        // given
        when(commandProductTagRepository.save(any())).thenReturn(productTag);

        // when
        ProductTagResponseDto response = service.register(productTag);

        // then
        assertThat(response.getProduct()).isNotNull();
        assertThat(response.getProduct().getISBN()).isEqualTo(ISBN);
        assertThat(response.getTag()).isNotNull();
        assertThat(response.getTag().getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("태그 관계 삭제 성공")
    void deleteByProduct_success() {
        // given
        when(queryProductTagRepository.existsByProduct(any())).thenReturn(true);

        // when
        service.deleteByProduct(product);

        // then
        verify(commandProductTagRepository, times(1)).deleteByProduct(product);
    }

    @Test
    @DisplayName("태그 관계 삭제_상품과 연관된 태그 관계가 존재하지 않으면 아무것도 하지 않음")
    void deleteByProduct_notExistsByProduct() {
        // given
        when(queryProductTagRepository.existsByProduct(any())).thenReturn(false);

        // when
        service.deleteByProduct(product);

        // then
        verify(commandProductTagRepository, never()).deleteByProduct(product);
    }
}
