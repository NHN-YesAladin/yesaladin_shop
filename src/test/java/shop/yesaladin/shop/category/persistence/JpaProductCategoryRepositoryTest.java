package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.dummy.ProductCategoryDummy;
import shop.yesaladin.shop.category.exception.ProductCategoryNotFoundException;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.publisher.domain.model.Publisher;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaProductCategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaProductCategoryRepository repository;

    private String name = "국내도서";
    String isbn = "00000-000XX-XXX-XXX";

    ProductCategory productCategory;

    SubscribeProduct subscribeProduct;
    Publisher publisher;
    File thumbNailFile;
    File ebookFile;
    TotalDiscountRate totalDiscountRate;

    @BeforeEach
    void setUp() {
        subscribeProduct = entityManager.persist(DummySubscribeProduct.dummy());
        publisher = entityManager.persist(DummyPublisher.dummy());
        thumbNailFile = entityManager.persist(DummyFile.dummy(".png"));
        ebookFile = entityManager.persist(DummyFile.dummy(".pdf"));
        totalDiscountRate = entityManager.persist(DummyTotalDiscountRate.dummy());

        Product product = DummyProduct.dummy(isbn,
                subscribeProduct,
                publisher,
                thumbNailFile,
                ebookFile,
                totalDiscountRate);
        Category category = CategoryDummy.dummyParent();

        entityManager.persist(product);
        entityManager.persist(category);

        productCategory = ProductCategoryDummy.dummy(category, product);

        System.out.println("productCategory = " + productCategory);
    }

    @Test
    void save() throws Exception {
        // when
        ProductCategory savedProductCategory = repository.save(productCategory);

        // then
        assertThat(savedProductCategory.getPk()
                .getCategoryId()).isEqualTo(productCategory.getCategory().getId());
        assertThat(savedProductCategory.getPk()
                .getProductId()).isEqualTo(productCategory.getProduct().getId());
    }

    @Test
    void findByPk() throws Exception {
        // given
        entityManager.persist(productCategory);

        // when
        ProductCategory foundProductCategory = repository.findByPk(new Pk(
                productCategory.getCategory().getId(),
                productCategory.getProduct().getId()
        )).orElseThrow(() -> new ProductCategoryNotFoundException(productCategory.getPk()));

        // then
        assertThat(foundProductCategory.getCategory()).isEqualTo(productCategory.getCategory());
        assertThat(foundProductCategory.getProduct()).isEqualTo(productCategory.getProduct());
    }

    @Test
    void deletedByPk() throws Exception {
        // given
        entityManager.persist(productCategory);

        // when
        repository.deleteByPk(new Pk(
                productCategory.getCategory().getId(),
                productCategory.getProduct().getId()
        ));

        // then
        assertThatThrownBy(() -> repository.findByPk(new Pk(
                        productCategory.getCategory().getId(),
                        productCategory.getProduct().getId()
                ))
                .orElseThrow(() -> new ProductCategoryNotFoundException(productCategory.getPk()))).isInstanceOf(
                ProductCategoryNotFoundException.class);
    }

    @Test
    void findAll_pageable() throws Exception {
        // given
        int size = 3;
        for (int i = 0; i < 5; i++) {
            Product product = DummyProduct.dummy(
                    isbn + i,
                    subscribeProduct,
                    publisher,
                    thumbNailFile,
                    ebookFile,
                    totalDiscountRate
            );
            Category category = CategoryDummy.dummyParent((long) i);

            entityManager.persist(product);
            entityManager.persist(category);

            productCategory = ProductCategoryDummy.dummy(category, product);
            entityManager.persist(productCategory);
        }
        PageRequest pageRequest = PageRequest.of(0, size);

        // when
        Page<ProductCategory> productCategoryPage = repository.findAll(pageRequest);

        // then
        assertThat(productCategoryPage.getContent().size()).isEqualTo(size);
    }
}
