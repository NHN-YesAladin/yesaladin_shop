package shop.yesaladin.shop.publish.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PublisherTest {

    @Test
    @DisplayName("출판사 이름 변경 성공")
    void changeName() {
        // given
        String name = "출판사2";
        Publisher publisher = Publisher.builder().id(1L).name("출판사1").build();

        // when
        publisher.changeName(name);

        // then
        assertThat(publisher.getName()).isEqualTo(name);
    }
}