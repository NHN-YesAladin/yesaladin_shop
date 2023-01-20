package shop.yesaladin.shop.writing.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.domain.repository.QueryWritingRepository;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class QueryWritingServiceImplTest {

    private final String ISBN = "0000000000001";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private QueryWritingService service;

    private QueryWritingRepository queryWritingRepository;

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


        queryWritingRepository = mock(QueryWritingRepository.class);

        service = new QueryWritingServiceImpl(
                queryWritingRepository
        );
    }

    @Test
    @DisplayName("상품으로 집필 조회 성공")
    void findByProduct() {
        // given
        List<Writing> writings = Arrays.asList(
                Writing.create(product, DummyAuthor.dummy("저자1", MemberDummy.dummyWithId(1L))),
                Writing.create(product, DummyAuthor.dummy("저자2", null))
        );

        Mockito.when(queryWritingRepository.findByProduct(product)).thenReturn(writings);

        // when
        List<WritingResponseDto> response = service.findByProduct(product);

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getProduct()).isEqualTo(product);
        assertThat(response.get(1).getProduct()).isEqualTo(product);
        assertThat(response.get(0).getAuthor().getName()).isEqualTo("저자1");
        assertThat(response.get(1).getAuthor().getName()).isEqualTo("저자2");
        assertThat(response.get(1).getAuthor().getMember()).isNull();
    }
}