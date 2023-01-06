package shop.yesaladin.shop.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.file.persistence.JpaFileRepository;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.publisher.persistence.JpaPublisherRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaProductRepositoryTest {

    @Autowired
    private JpaSubscribeProductRepository jpaSubscribeProductRepository;
    @Autowired
    private JpaPublisherRepository jpaPublisherRepository;
    @Autowired
    private JpaFileRepository jpaFileRepository;
    @Autowired
    private JpaTotalDiscountRateRepository jpaTotalDiscountRateRepository;
    @Autowired
    private JpaProductRepository jpaProductRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        jpaSubscribeProductRepository.save(DummySubscribeProduct.dummy());
        jpaPublisherRepository.save(DummyPublisher.dummy());
        jpaFileRepository.save(DummyFile.dummy());
        jpaTotalDiscountRateRepository.save(DummyTotalDiscountRate.dummy());

        product = DummyProduct.dummy();
    }

    @Test
    void save() {
        // when
        Product savedProduct = jpaProductRepository.save(product);

        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    void findById() {
        // given
        Long id = 1L;
        jpaProductRepository.save(product);

        // when
        Optional<Product> foundProduct = jpaProductRepository.findById(id);

        // then
        assertThat(foundProduct).isPresent();
    }

}
