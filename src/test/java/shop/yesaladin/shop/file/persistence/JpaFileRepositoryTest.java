package shop.yesaladin.shop.file.persistence;

import static org.assertj.core.api.Assertions.assertThat;

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

    private final String EXTENSION = ".jpg";

    @Autowired
    private JpaFileRepository jpaFileRepository;

    private File file;

    @BeforeEach
    void setUp() {
        file = DummyFile.dummy();
    }

    @Test
    void save() {
        // when
        File savedFile = jpaFileRepository.save(file);

        // then
        assertThat(savedFile).isNotNull();
        assertThat(savedFile.getExtension()).isEqualTo(EXTENSION);
    }
}
