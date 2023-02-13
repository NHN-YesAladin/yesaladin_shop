package shop.yesaladin.shop.tag.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.tag.domain.repository.SearchTagRepository;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;

@SpringBootTest
@ActiveProfiles("local-test")
class ElasticTagSearchRepositoryTest {
    @Autowired
    SearchTagRepository searchTagRepository;

    @Test
    @DisplayName("태그 이름으로 검색 테스트")
    void test() {
        SearchedTagResponseDto responseDto = searchTagRepository.searchTagByName("ㄷㄱㅈㄱㄷㅈㄷㄱ", 0, 1);
        assertThat(responseDto.getCount()).isZero();
        assertThat(responseDto.getSearchedTagDtoList()).isEmpty();
    }
}