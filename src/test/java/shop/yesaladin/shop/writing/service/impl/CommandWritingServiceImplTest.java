package shop.yesaladin.shop.writing.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.domain.repository.CommandWritingRepository;
import shop.yesaladin.shop.writing.domain.repository.QueryWritingRepository;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;

class CommandWritingServiceImplTest {

    private final String ISBN = "0000000000001";
    private final String NAME = "저자1";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private CommandWritingService service;
    private CommandWritingRepository commandWritingRepository;
    private QueryWritingRepository queryWritingRepository;

    private Product product;
    private Author author;
    private Writing writing;

    @BeforeEach
    void setUp() {
        product = DummyProduct.dummy(
                ISBN,
                DummySubscribeProduct.dummy(),
                DummyFile.dummy(URL + "/image.png"),
                DummyFile.dummy(URL + "/ebook.pdf"),
                DummyTotalDiscountRate.dummy()
        );
        author = DummyAuthor.dummy(NAME, null);

        writing = Writing.create(product, author);

        commandWritingRepository = mock(CommandWritingRepository.class);
        queryWritingRepository = mock(QueryWritingRepository.class);

        service = new CommandWritingServiceImpl(
                commandWritingRepository,
                queryWritingRepository
        );
    }

    @Test
    @DisplayName("집필 등록 성공")
    void create() {
        // given
        when(commandWritingRepository.save(any())).thenReturn(writing);

        // when
        WritingResponseDto response = service.register(writing);

        // then
        assertThat(response.getProduct()).isNotNull();
        assertThat(response.getProduct().getIsbn()).isEqualTo(ISBN);
        assertThat(response.getAuthor()).isNotNull();
        assertThat(response.getAuthor().getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("집필 삭제 성공")
    void deleteByProduct_success() {
        // given
        when(queryWritingRepository.existsByProduct(any())).thenReturn(true);

        // when then
        assertThatCode(() -> service.deleteByProduct(product))
                .doesNotThrowAnyException();

        verify(commandWritingRepository, times(1)).deleteByProduct(product);
    }

    @Test
    @DisplayName("집필 삭제 실패_존재하지 않는 집필을 삭제하려 할 때 예외 발생")
    void deleteByProduct_notExistsByProduct_throwWritingNotFoundException() {
        // given
        when(queryWritingRepository.existsByProduct(any())).thenReturn(false);

        // when then
        assertThatCode(() -> service.deleteByProduct(product)).isInstanceOf(ClientException.class);

        verify(commandWritingRepository, never()).deleteByProduct(product);
    }
}
