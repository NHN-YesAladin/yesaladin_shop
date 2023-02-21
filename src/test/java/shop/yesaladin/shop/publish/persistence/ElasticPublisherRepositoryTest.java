package shop.yesaladin.shop.publish.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto;

@SpringBootTest
@ActiveProfiles("local-test")
class ElasticPublisherRepositoryTest {

    @Autowired
    ElasticPublisherRepository elasticPublisherRepository;

    @Test
    void testSearchPublisherByName() {
        SearchPublisherResponseDto result = elasticPublisherRepository.searchPublisherByName(
                "dfs!@!3",
                0,
                1
        );
        assertThat(result.getCount()).isZero();
        assertThat(result.getSearchedPublisherDtoList()).isEmpty();
    }
}