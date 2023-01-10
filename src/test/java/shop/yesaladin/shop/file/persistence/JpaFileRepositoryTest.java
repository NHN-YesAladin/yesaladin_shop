package shop.yesaladin.shop.file.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.dummy.DummyFile;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaFileRepositoryTest {

    private final String FILE_NAME = "UUID.png";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaFileRepository jpaFileRepository;

    private File file;

    @BeforeEach
    void setUp() {
        file = DummyFile.dummy("png");
    }

    @Test
    void save() {
        // when
        File savedFile = jpaFileRepository.save(file);

        // then
        assertThat(savedFile).isNotNull();
        assertThat(savedFile.getName()).isEqualTo(FILE_NAME);
    }

    @Test
    void findByName() {
        // given
        entityManager.persist(file);

        // when
        Optional<File> foundFile = jpaFileRepository.findByName(FILE_NAME);

        // then
        assertThat(foundFile).isPresent();
        assertThat(foundFile.get().getName()).isEqualTo(FILE_NAME);
    }
}
