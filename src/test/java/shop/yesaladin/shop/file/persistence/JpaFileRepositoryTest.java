package shop.yesaladin.shop.file.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.dummy.DummyFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("local-test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaFileRepositoryTest {

    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type/image.jpg";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaFileRepository repository;

    private File file;

    @BeforeEach
    void setUp() {
        file = DummyFile.dummy(URL);
    }

    @Test
    @DisplayName("파일 저장")
    void save() {
        // when
        File savedFile = repository.save(file);

        // then
        assertThat(savedFile).isNotNull();
        assertThat(savedFile.getUrl()).isEqualTo(URL);
    }

    @Test
    @DisplayName("파일 ID로 조회")
    void findById() {
        // given
        File savedFile = entityManager.persist(file);

        // when
        Optional<File> optionalFile = repository.findById(savedFile.getId());

        // then
        assertThat(optionalFile).isPresent();
        assertThat(optionalFile.get().getUrl()).isEqualTo(file.getUrl());
    }

}
