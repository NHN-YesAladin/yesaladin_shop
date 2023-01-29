package shop.yesaladin.shop.product.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.exception.ProductTypeCodeNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @DisplayName("ID로 상품 조회")
    void findById() {
        // given
        entityManager.persist(product1);
        Long id = product1.getId();

        // when
        Optional<Product> optionalProduct = repository.findById(id);

        // then
        assertThat(optionalProduct).isPresent();
        assertThat(optionalProduct.get().getISBN()).isEqualTo(ISBN1);
        assertThat(optionalProduct.get().getThumbnailFile()).isEqualTo(thumbnailFile1);
        assertThat(optionalProduct.get().getEbookFile()).isEqualTo(ebookFile1);
        assertThat(optionalProduct.get().getProductTypeCode()).isEqualTo(ProductTypeCode.BESTSELLER);
    }

    @Test
    @DisplayName("ISBN으로 상품 조회")
    void findByISBN() {
        // given
        entityManager.persist(product1);

        // when
        Optional<Product> optionalProduct = repository.findByISBN(ISBN1);

        // then
        assertThat(optionalProduct).isPresent();
        assertThat(optionalProduct.get().getISBN()).isEqualTo(ISBN1);
        assertThat(optionalProduct.get().getThumbnailFile()).isEqualTo(thumbnailFile1);
        assertThat(optionalProduct.get().getEbookFile()).isEqualTo(ebookFile1);
        assertThat(optionalProduct.get().getProductTypeCode()).isEqualTo(ProductTypeCode.BESTSELLER);
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
        assertThat(products.getContent().get(0).getISBN()).isEqualTo(ISBN1);
        assertThat(products.getContent().get(0).getThumbnailFile()).isEqualTo(thumbnailFile1);
        assertThat(products.getContent().get(0).getEbookFile()).isEqualTo(ebookFile1);
        assertThat(products.getContent().get(0).getProductTypeCode()).isEqualTo(ProductTypeCode.BESTSELLER);
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
        assertThat(products.getTotalElements()).isEqualTo(1);
        assertThat(products.getTotalPages()).isEqualTo(1);
        assertThat(products.getContent().get(0).getISBN()).isEqualTo(ISBN1);
        assertThat(products.getContent().get(0).getThumbnailFile()).isEqualTo(thumbnailFile1);
        assertThat(products.getContent().get(0).getEbookFile()).isEqualTo(ebookFile1);
        assertThat(products.getContent().get(0).getProductTypeCode()).isEqualTo(ProductTypeCode.BESTSELLER);
    }

    @Test
    @DisplayName("상품 유형별 조회_성공")
    void findAllByTypeId_success() {
        // given
        entityManager.persist(product1);
        entityManager.persist(product2);

        // when
        Page<Product> products = repository.findAllByTypeId(PageRequest.of(0, 5), ProductTypeCode.NEWBOOK.getId());

        // then
        assertThat(products).isNotNull();
        assertThat(products.getTotalElements()).isEqualTo(1);
        assertThat(products.getContent().get(0).getISBN()).isEqualTo(ISBN2);
        assertThat(products.getContent().get(0).getThumbnailFile()).isEqualTo(thumbnailFile2);
        assertThat(products.getContent().get(0).getEbookFile()).isEqualTo(ebookFile2);
        assertThat(products.getContent().get(0).getProductTypeCode()).isEqualTo(ProductTypeCode.NEWBOOK);
    }

    @Test
    @DisplayName("상품 유형별 조회_존재하지 않는 상품유형으로 검색하는 경우 발생")
    void findAllByTypeId_notExistProductType_throwProductTypeCodeNotFoundException() {
        // given
        entityManager.persist(product1);
        entityManager.persist(product2);

        // when
        assertThatThrownBy(() -> repository.findAllByTypeId(PageRequest.of(0, 5), 10))
                .isInstanceOf(ProductTypeCodeNotFoundException.class);
    }
}