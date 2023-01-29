package shop.yesaladin.shop.tag.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.yesaladin.shop.tag.domain.repository.SearchTagRepository;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;

@SpringBootTest
class ElasticTagSearchRepositoryTest {
    @Autowired
    SearchTagRepository searchTagRepository;

    @Test
    @DisplayName("태그 이름으로 검색 테스트")
    void test() {
        List<TagsResponseDto> response = searchTagRepository.searchTagByName("ㄷㄱㅈㄱㄷㅈㄷㄱ");
        assertThat(response).isEmpty();
    }
}