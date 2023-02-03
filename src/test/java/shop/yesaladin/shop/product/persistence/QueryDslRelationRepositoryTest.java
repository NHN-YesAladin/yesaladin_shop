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
import shop.yesaladin.shop.product.domain.model.Relation;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.QueryRelationRepository;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class QueryDslRelationRepositoryTest {

    private final String ISBN = "000000000000";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private QueryRelationRepository repository;

    private TotalDiscountRate totalDiscountRate;

    private final List<Product> products = new ArrayList<>();

    @BeforeEach
    void setUp() {
        totalDiscountRate = DummyTotalDiscountRate.dummy();
        entityManager.persist(totalDiscountRate);

        for (int i = 0; i < 10; i++) {
            File thumbnailFile = DummyFile.dummy(URL + i);
            entityManager.persist(thumbnailFile);

            boolean isDeleted = i % 2 != 0;
            Product product = DummyProduct.dummy(
                    ISBN + i,
                    null,
                    thumbnailFile,
                    null,
                    totalDiscountRate,
                    isDeleted
            );
            entityManager.persist(product);
            products.add(product);
        }

    }

    @Test
    @DisplayName("연관관계 Pk로 연관관계의 존재여부 확인")
    void existsByPk() {
        // given
        Relation relation = Relation.create(products.get(0), products.get(1));
        entityManager.persist(relation);

        // when
        boolean isExist = repository.existsByPk(relation.getPk());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("관리자용 연관관계 전체 조회")
    void findAllForManager() {
        // given
        for (int i = 1; i < 10; i++) {
            Relation relation = Relation.create(products.get(0), products.get(i));
            entityManager.persist(relation);
        }

        // when
        Page<Relation> relations = repository.findAllForManager(products.get(0).getId(), PageRequest.of(0, 5));

        // then
        assertThat(relations).isNotNull();
        assertThat(relations.getTotalElements()).isEqualTo(9);
        assertThat(relations.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("일반 사용자용 연관관계 전체 조회")
    void findAll() {
        // given
        for (int i = 1; i < 10; i++) {
            Relation relation = Relation.create(products.get(0), products.get(i));
            entityManager.persist(relation);
        }

        // when
        Page<Relation> relations = repository.findAll(products.get(0).getId(), PageRequest.of(0, 5));

        // then
        assertThat(relations).isNotNull();
        assertThat(relations.getTotalElements()).isEqualTo(4);
        assertThat(relations.getTotalPages()).isEqualTo(1);
    }
}