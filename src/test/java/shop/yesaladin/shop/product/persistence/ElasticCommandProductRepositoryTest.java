package shop.yesaladin.shop.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;

@SpringBootTest
@ActiveProfiles("local-test")
class ElasticCommandProductRepositoryTest {
    @Autowired
    private ElasticCommandProductRepository elasticCommandProductRepository;

    @Test
    void update() {
        SearchedProduct before = SearchedProduct.builder().id(-1L).title("before").build();
        elasticCommandProductRepository.save(before);
        SearchedProduct after = SearchedProduct.builder().id(-1L).title("after").build();
        elasticCommandProductRepository.save(after);
        Optional<SearchedProduct> result = elasticCommandProductRepository.findById(before.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(after.getTitle());
        elasticCommandProductRepository.delete(result.get());
    }

    @Test
    void delete() {
        SearchedProduct before = SearchedProduct.builder().id(-1L).title("before").build();
        elasticCommandProductRepository.save(before);
        elasticCommandProductRepository.deleteByIdEquals(before.getId());
        Optional<SearchedProduct> result = elasticCommandProductRepository.findById(before.getId());
        assertThat(result).isNotPresent();
    }
}