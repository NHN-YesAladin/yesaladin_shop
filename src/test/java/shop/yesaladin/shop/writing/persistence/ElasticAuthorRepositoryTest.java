package shop.yesaladin.shop.writing.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto;

@SpringBootTest
class ElasticAuthorRepositoryTest {
    @Autowired
    private ElasticAuthorRepository elasticAuthorRepository;

    @Test
    void searchAuthorByName() {
        SearchedAuthorResponseDto dto = elasticAuthorRepository.searchAuthorByName("fsdfsg", 0, 1);
        assertThat(dto).isNotNull();
    }
}