package shop.yesaladin.shop.writing.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.writing.domain.model.Writing;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaWritingRepositoryTest {

    private final String AUTHOR_NAME = "홍길동";
    private final String ISBN = "00001-...";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaWritingRepository jpaWritingRepository;

    private Writing writing;

    @BeforeEach
    void setUp() {
        Product product = DummyProduct.dummy(ISBN);
        Member member = MemberDummy.dummy();

        entityManager.persist(product);
        entityManager.persist(member);

//        writing = Writing.builder().authorName(AUTHOR_NAME).product(product).member(member).build();
    }

    @Disabled
    @Test
    void save() {
        // when
        Writing savedWriting = jpaWritingRepository.save(writing);

        // then
        assertThat(savedWriting).isNotNull();
//        assertThat(savedWriting.getAuthorName()).isEqualTo(AUTHOR_NAME);
    }
}
