package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.repository.QuerySubscribeProductRepository;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.service.inter.QuerySubscribeProductService;

class QuerySubscribeProductServiceImplTest {

    private final String ISSN = "0000-XXXX";

    private QuerySubscribeProductService querySubscribeProductService;
    private QuerySubscribeProductRepository querySubscribeProductRepository;

    private SubscribeProduct subscribeProduct;

    @BeforeEach
    void setUp() {
        subscribeProduct = DummySubscribeProduct.dummy();

        querySubscribeProductRepository = mock(QuerySubscribeProductRepository.class);
        querySubscribeProductService = new QuerySubscribeProductServiceImpl(querySubscribeProductRepository);
    }

    @Test
    void findByISSN() {
        // given
        when(querySubscribeProductRepository.findByISSN(any())).thenReturn(Optional.of(subscribeProduct));

        // when
        SubscribeProduct foundSubscribeProduct = querySubscribeProductService.findByISSN(ISSN);

        // then
        assertThat(foundSubscribeProduct).isNotNull();
        assertThat(foundSubscribeProduct.getISSN()).isEqualTo(ISSN);
    }
}
