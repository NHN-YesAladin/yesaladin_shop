package shop.yesaladin.shop.product.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dto.ProductOnlyTitleDto;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class QueryDslProductRepositoryTest {

    private final String ISBN1 = "0000000000001";
    private final String ISBN2 = "0000000000002";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QueryDslProductRepository repository;

    private File thumbnailFile1;
    private File thumbnailFile2;
    private File ebookFile1;
    private File ebookFile2;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        entityManager.persist(subscribeProduct);

        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();
        entityManager.persist(totalDiscountRate);

        thumbnailFile1 = DummyFile.dummy(URL + "/image1.png");
        thumbnailFile2 = DummyFile.dummy(URL + "/image2.png");
        entityManager.persist(thumbnailFile1);
        entityManager.persist(thumbnailFile2);

        ebookFile1 = DummyFile.dummy(URL + "/ebook1.pdf");
        ebookFile2 = DummyFile.dummy(URL + "/ebook2.pdf");
        entityManager.persist(ebookFile1);
        entityManager.persist(ebookFile2);

        product1 = DummyProduct.dummy(
                ISBN1,
                subscribeProduct,
                thumbnailFile1,
                ebookFile1,
                totalDiscountRate,
                ProductTypeCode.BESTSELLER
        );
        product2 = DummyProduct.dummy(
                ISBN2,
                subscribeProduct,
                thumbnailFile2,
                ebookFile2,
                totalDiscountRate,
                ProductTypeCode.NEWBOOK
        );
    }

    @Test
    @DisplayName("상품 ISBN으로 상품 제목 조회")
    void findTitleByIsbn() {
        // given
        entityManager.persist(product1);

        // when
        ProductOnlyTitleDto response = repository.findTitleByIsbn(ISBN1);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(product1.getTitle());
    }

    @Test
    @DisplayName("상품 ID로 상품 수량 조회")
    void findQuantityById() {
        // given
        entityManager.persist(product1);

        // when
        Long response = repository.findQuantityById(product1.getId());

        // then
        assertThat(response).isEqualTo(product1.getQuantity());
    }

    @Test
    @DisplayName("ID로 상품 조회")
    void findById() {
        // given
        entityManager.persist(product1);
        Long id = product1.getId();

        // when
        Optional<Product> optionalProduct = repository.findProductById(id);

        // then
        assertThat(optionalProduct).isPresent();
        assertThat(optionalProduct.get().getIsbn()).isEqualTo(ISBN1);
        assertThat(optionalProduct.get().getThumbnailFile()).isEqualTo(thumbnailFile1);
        assertThat(optionalProduct.get().getEbookFile()).isEqualTo(ebookFile1);
        assertThat(optionalProduct.get()
                .getProductTypeCode()).isEqualTo(ProductTypeCode.BESTSELLER);
    }

    @Test
    @DisplayName("ISBN으로 상품 조회")
    void findByIsbn() {
        // given
        entityManager.persist(product1);

        // when
        Optional<Product> optionalProduct = repository.findByIsbn(ISBN1);

        // then
        assertThat(optionalProduct).isPresent();
        assertThat(optionalProduct.get().getIsbn()).isEqualTo(ISBN1);
        assertThat(optionalProduct.get().getThumbnailFile()).isEqualTo(thumbnailFile1);
        assertThat(optionalProduct.get().getEbookFile()).isEqualTo(ebookFile1);
        assertThat(optionalProduct.get()
                .getProductTypeCode()).isEqualTo(ProductTypeCode.BESTSELLER);
    }

    @Test
    @DisplayName("상품 전체 사용자용 전체 조회")
    void findAll() {
        // given
        entityManager.persist(product1);
        product2.deleteProduct();
        entityManager.persist(product2);

        // when
        Page<Product> products = repository.findAll(PageRequest.of(0, 5));

        // then
        assertThat(products).isNotNull();
        assertThat(products.getTotalElements()).isEqualTo(1);
        assertThat(products.getContent().get(0).getIsbn()).isEqualTo(ISBN1);
        assertThat(products.getContent().get(0).getThumbnailFile()).isEqualTo(thumbnailFile1);
        assertThat(products.getContent().get(0).getEbookFile()).isEqualTo(ebookFile1);
        assertThat(products.getContent()
                .get(0)
                .getProductTypeCode()).isEqualTo(ProductTypeCode.BESTSELLER);
    }

    @Test
    @DisplayName("상품 관리자용 전체 조회")
    void findAllForManager() {
        // given
        entityManager.persist(product1);
        entityManager.persist(product2);

        // when
        Page<Product> products = repository.findAllForManager(PageRequest.of(0, 1));

        // then
        assertThat(products).isNotNull();
        assertThat(products.getTotalElements()).isEqualTo(2);
        assertThat(products.getTotalPages()).isEqualTo(2);
        assertThat(products.getContent().get(0).getIsbn()).isEqualTo(ISBN1);
        assertThat(products.getContent().get(0).getThumbnailFile()).isEqualTo(thumbnailFile1);
        assertThat(products.getContent().get(0).getEbookFile()).isEqualTo(ebookFile1);
        assertThat(products.getContent()
                .get(0)
                .getProductTypeCode()).isEqualTo(ProductTypeCode.BESTSELLER);
    }

    @Test
    @DisplayName("상품 유형별 조회_성공")
    void findAllByTypeId_success() {
        // given
        entityManager.persist(product1);
        entityManager.persist(product2);

        // when
        Page<Product> products = repository.findAllByTypeId(
                PageRequest.of(0, 5),
                ProductTypeCode.NEWBOOK.getId()
        );

        // then
        assertThat(products).isNotNull();
        assertThat(products.getTotalElements()).isEqualTo(1);
        assertThat(products.getContent().get(0).getIsbn()).isEqualTo(ISBN2);
        assertThat(products.getContent().get(0).getThumbnailFile()).isEqualTo(thumbnailFile2);
        assertThat(products.getContent().get(0).getEbookFile()).isEqualTo(ebookFile2);
        assertThat(products.getContent()
                .get(0)
                .getProductTypeCode()).isEqualTo(ProductTypeCode.NEWBOOK);
    }

    @Test
    @DisplayName("상품 유형별 조회_존재하지 않는 상품유형으로 검색하는 경우 발생")
    void findAllByTypeId_notExistProductType_throwProductTypeCodeNotFoundException() {
        // given
        entityManager.persist(product1);
        entityManager.persist(product2);

        // when
        assertThatThrownBy(() -> repository.findAllByTypeId(PageRequest.of(0, 5), 10))
                .isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("상품 유형별 조회_성공")
    void findAllByTypeIdForManager_success() {
        // given
        entityManager.persist(product1);
        entityManager.persist(product2);

        // when
        Page<Product> products = repository.findAllByTypeIdForManager(PageRequest.of(0, 5), ProductTypeCode.NEWBOOK.getId());

        // then
        assertThat(products).isNotNull();
        assertThat(products.getTotalElements()).isEqualTo(1);
        assertThat(products.getContent().get(0).getIsbn()).isEqualTo(ISBN2);
        assertThat(products.getContent().get(0).getThumbnailFile()).isEqualTo(thumbnailFile2);
        assertThat(products.getContent().get(0).getEbookFile()).isEqualTo(ebookFile2);
        assertThat(products.getContent().get(0).getProductTypeCode()).isEqualTo(ProductTypeCode.NEWBOOK);
    }

    @Test
    @DisplayName("상품 유형별 조회_존재하지 않는 상품유형으로 검색하는 경우 발생")
    void findAllByTypeIdForManager_notExistProductType_throwProductTypeCodeNotFoundException() {
        // given
        entityManager.persist(product1);
        entityManager.persist(product2);

        // when
        assertThatThrownBy(() -> repository.findAllByTypeIdForManager(PageRequest.of(0, 5), 10))
                .isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("주문에 필요한 상품 데이터 조회")
    void getProductForOrder() {
//        //given
//        List<OrderProductRequestDto> request = getOrderProductRequestData();
//        Product product = DummyProduct.dummy()
//        //when
//
//
//        //then
    }

    @Test
    @DisplayName("상품의 연관 상품 등록을 위한 검색 성공")
    void findProductRelationByTitle() {
        //given
        entityManager.persist(product1);
        entityManager.persist(product2);

        //when
        Page<Product> products = repository.findProductRelationByTitle(
                product1.getId(),
                product1.getTitle().substring(0, 1),
                PageRequest.of(0, 10)
        );

        //then
        assertThat(products.getTotalElements()).isEqualTo(1);
        assertThat(products.getContent()
                .get(0)
                .getTitle()
                .contains(product1.getTitle().substring(0, 1))).isTrue();

    }

    @Test
    @DisplayName("최신 상품 조회 성공")
    public void findRecentProductByPublishedDate() {
        //given
        Publisher publisher = Publisher.builder().id(1L).name("name1").build();
        Publish publish1 = Publish.create(product1, publisher, "2011-01-01");
        Publish publish2 = Publish.create(product2, publisher, "2011-02-02");

        entityManager.persist(product2);
        entityManager.persist(product1);

        //when
        Page<Product> products = repository.findRecentProductByPublishedDate(PageRequest.of(0, 10));

        //then
        assertThat(products.getTotalElements()).isEqualTo(2);
        assertThat(products.getContent().get(0).getId()).isEqualTo(product2.getId());
        assertThat(products.getContent().get(1).getId()).isEqualTo(product1.getId());
    }

    private List<ProductOrderRequestDto> getOrderProductRequestData() {
        List<ProductOrderRequestDto> request = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            request.add(ReflectionUtils.newInstance(
                    ProductOrderRequestDto.class,
                    "103-341",
                    2
            ));
        }
        return request;
    }
}