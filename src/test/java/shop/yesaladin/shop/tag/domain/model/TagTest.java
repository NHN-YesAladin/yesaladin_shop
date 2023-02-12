package shop.yesaladin.shop.tag.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    @DisplayName("태그 이름 변경 성공")
    void changeName() {
        // given
        String name = "슬픈";
        Tag tag = Tag.builder().id(1L).name("아름다운").build();

        // when
        tag.changeName(name);

        // then
        assertThat(tag.getName()).isEqualTo(name);

    }
}