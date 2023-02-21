package shop.yesaladin.shop.tag.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryProductTagRepository;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryProductTagService;

class QueryProductTagServiceImplTest {

    private final String ISBN = "0000000000001";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private QueryProductTagService service;
    private QueryProductTagRepository queryProductTagRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = DummyProduct.dummy(
                ISBN,
                DummySubscribeProduct.dummy(),
                DummyFile.dummy(URL + "/image.png"),
                DummyFile.dummy(URL + "/ebook.pdf"),
                DummyTotalDiscountRate.dummy()
        );

        queryProductTagRepository = mock(QueryProductTagRepository.class);

        service = new QueryProductTagServiceImpl(
                queryProductTagRepository
        );
    }

    @Test
    @DisplayName("상품으로 태그 관계 조회 성공")
    void findByProduct() {
        // given
        String name1 = "행복한";
        String name2 = "슬픈";

        List<ProductTag> productTags = List.of(
                ProductTag.create(product, Tag.builder().name(name1).build()),
                ProductTag.create(product, Tag.builder().name(name2).build())
        );

        Mockito.when(queryProductTagRepository.findByProduct(product)).thenReturn(productTags);

        // when
        List<ProductTagResponseDto> response = service.findByProduct(product);

        // then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getProduct()).isEqualTo(product);
        assertThat(response.get(1).getProduct()).isEqualTo(product);
        assertThat(response.get(0).getTag().getName()).isEqualTo(name1);
        assertThat(response.get(1).getTag().getName()).isEqualTo(name2);
    }
}