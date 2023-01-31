package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.yesaladin.shop.category.domain.repository.SearchCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;

@SpringBootTest
class ElasticCategoryRepositoryTest {
    @Autowired
    private SearchCategoryRepository searchCategoryRepository;

    @Test
    void testSearchCategoryByName() {
        SearchCategoryResponseDto result = searchCategoryRepository.searchCategoryByName("ㅇ러ㅏㅇ러ㅏㅇ", 0, 1);
        assertThat(result.getCount()).isZero();
        assertThat(result.getSearchedCategoryDtoList()).isEmpty();
    }
}