package shop.yesaladin.shop.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandRelationRepository;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.domain.repository.QueryRelationRepository;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.exception.RelationAlreadyExistsException;
import shop.yesaladin.shop.product.exception.RelationNotFoundException;
import shop.yesaladin.shop.product.service.inter.CommandRelationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandRelationServiceImplTest {

    private final String ISBN = "000000000000";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";
    private final Long MAIN_ID = 1L;
    private final Long SUB_ID = 2L;

    private CommandRelationService service;

    private CommandRelationRepository commandRelationRepository;
    private QueryRelationRepository queryRelationRepository;
    private QueryProductRepository queryProductRepository;

    private TotalDiscountRate totalDiscountRate;

    private final List<Product> products = new ArrayList<>();

    @BeforeEach
    void setUp() {
        commandRelationRepository = mock(CommandRelationRepository.class);
        queryRelationRepository = mock(QueryRelationRepository.class);
        queryProductRepository = mock(QueryProductRepository.class);

        service = new CommandRelationServiceImpl(
                commandRelationRepository,
                queryRelationRepository,
                queryProductRepository
        );

        totalDiscountRate = DummyTotalDiscountRate.dummy();

        for (int i = 0; i < 2; i++) {
            File thumbnailFile = DummyFile.dummy(URL + i);

            boolean isDeleted = i % 2 != 0;
            Product product = DummyProduct.dummy(
                    ISBN + i,
                    null,
                    thumbnailFile,
                    null,
                    totalDiscountRate,
                    isDeleted
            );
            products.add(product);
        }
    }

    @Test
    @DisplayName("상품 연관관계 생성 성공")
    void create_success() {
        // given
        Product productMain = products.get(0);
        Product productSub = products.get(1);

        Mockito.when(queryRelationRepository.existsByPk(any())).thenReturn(false);

        Mockito.when(queryProductRepository.findById(MAIN_ID)).thenReturn(Optional.of(productMain));
        Mockito.when(queryProductRepository.findById(SUB_ID)).thenReturn(Optional.ofNullable(productSub));

        Mockito.when(queryProductRepository.findById(MAIN_ID)).thenReturn(Optional.of(productMain));
        Mockito.when(queryProductRepository.findById(SUB_ID)).thenReturn(Optional.of(productSub));

        // when
        ProductOnlyIdDto productMAIN_ID = service.create(MAIN_ID, SUB_ID);

        // then
        assertThat(productMAIN_ID).isNotNull();
        assertThat(productMAIN_ID.getId()).isEqualTo(MAIN_ID);

        verify(queryRelationRepository, times(2)).existsByPk(any());
        verify(queryProductRepository, times(1)).findById(MAIN_ID);
        verify(queryProductRepository, times(1)).findById(SUB_ID);

        verify(commandRelationRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("상품 연관관계가 이미 양쪽으로 존재하는 관계인 경우 생성 실패")
    void create_throwRelationAlreadyExistsException() {
        // given
        Mockito.when(queryRelationRepository.existsByPk(any())).thenReturn(true);

        // when
        assertThatThrownBy(() -> service.create(MAIN_ID, SUB_ID))
                .isInstanceOf(RelationAlreadyExistsException.class);
    }

    @Test
    @DisplayName("상품 연관관계 삭제 성공")
    void delete_success() {
        // given
        Mockito.when(queryRelationRepository.existsByPk(any())).thenReturn(true);

        // when
        service.delete(MAIN_ID, SUB_ID);

        // then
        verify(queryRelationRepository, times(2)).existsByPk(any());
        verify(commandRelationRepository, times(2)).deleteByPk(any());
    }

    @Test
    @DisplayName("상품 연관관계가 양쪽 모두 존재하지 않는 경우 삭제 실패")
    void delete_throwRelationNotFoundException() {
        // given
        Mockito.when(queryRelationRepository.existsByPk(any())).thenReturn(false);

        // when
        assertThatThrownBy(() -> service.delete(MAIN_ID, SUB_ID)).isInstanceOf(RelationNotFoundException.class);
    }
}