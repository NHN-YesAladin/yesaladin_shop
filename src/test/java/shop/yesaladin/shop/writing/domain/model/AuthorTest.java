package shop.yesaladin.shop.writing.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dummy.MemberDummy;

class AuthorTest {

    @Test
    @DisplayName("저자 멤버 변경 성공")
    void changeMember() {
        // given
        Author author = Author.builder().id(1L).name("저자1").member(null).build();
        Member member = MemberDummy.dummy();

        // when
        author.changeMember(member);

        // then
        assertThat(author.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("저자 이름 변경 성공")
    void changeName() {
        // given
        String name = "저자2";
        Author author = Author.builder().id(1L).name("저자1").member(null).build();

        // when
        author.changeName(name);

        // then
        assertThat(author.getName()).isEqualTo(name);
    }
}