package shop.yesaladin.shop.member.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import shop.yesaladin.common.exception.ClientException;

class MemberTest {

    @Test
    void unblockMember_failedWithAlreadyUnblockedMember() {
        //given
        Member blockedMember = Member.builder().id(1L).isBlocked(false).build();

        //when, then
        assertThatThrownBy(blockedMember::unblockMember).isInstanceOf(
                ClientException.class);
    }

    @Test
    void unblockMember() {
        //given
        Member blockedMember = Member.builder().id(1L).isBlocked(true).build();

        //when
        blockedMember.unblockMember();

        //then
        assertThat(blockedMember.isBlocked()).isFalse();
    }

    @Test
    void blockMember_failedWithAlreadyBlockedMember() {
        //given
        String blockedReason = "Bad Guy";
        Member unblockedMember = Member.builder().id(1L).isBlocked(true).build();

        //when, then
        assertThatThrownBy(() -> unblockedMember.blockMember(blockedReason))
                .isInstanceOf(ClientException.class);

    }

    @Test
    void blockMember() {
        //given
        String blockedReason = "Bad Guy";
        Member unblockedMember = Member.builder().id(1L).isBlocked(false).build();

        //when
        unblockedMember.blockMember(blockedReason);

        //then
        assertThat(unblockedMember.isBlocked()).isTrue();

    }

    @Test
    void changeNickname() {
        //given
        String newNickname = "new";
        Member member = Member.builder().nickname("origin").build();

        //when
        member.changeNickname(newNickname);

        //then
        assertThat(member.getNickname()).isEqualTo(newNickname);
    }

    @Test
    void isSameLoginId() throws Exception {
        //given
        Member member = Member.builder().loginId("test").build();
        Member compare = Member.builder().loginId("test").build();

        //when
        boolean result = member.isSameLoginId(compare);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void isSameNickname() throws Exception {
        //given
        Member member = Member.builder().nickname("test").build();
        Member compare = Member.builder().nickname("test").build();

        //when
        boolean result = member.isSameNickname(compare);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void changeName() {
        //given
        String newName = "new";
        Member member = Member.builder().name("origin").build();

        //when
        member.changeName(newName);

        //then
        assertThat(member.getName()).isEqualTo(newName);
    }

    @Test
    void changeEmail() throws Exception {
        //given
        String newEmail = "test@test.com";
        Member member = Member.builder().email("origin").build();

        //when
        member.changeEmail(newEmail);

        //then
        assertThat(member.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void changePhone() throws Exception {
        //given
        String newPhone = "01012345678";
        Member member = Member.builder().phone("origin").build();

        //when
        member.changePhone(newPhone);

        //then
        assertThat(member.getPhone()).isEqualTo(newPhone);
    }

    @Test
    void changePassword() throws Exception {
        //given
        String newPassword = "test";
        Member member = Member.builder().password("origin").build();

        //when
        member.changePassword(newPassword);

        //then
        assertThat(member.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void withdrawMember() throws Exception {
        //given
        String loginId = "id";
        String name = "name";
        String phone = "01011112222";
        String password = "password";

        Member member = Member.builder()
                .id(1L)
                .name(name)
                .loginId(loginId)
                .nickname(name)
                .password(password)
                .birthYear(1996)
                .birthMonth(1)
                .birthDay(19)
                .phone(phone)
                .build();

        //when
        member.withdrawMember();

        //then
        assertThat(member.isWithdrawal()).isTrue();
        assertThat(member.getWithdrawalDate()).isEqualTo(LocalDate.now());
        assertThat(member.getName()).isNotEqualTo(name);
        assertThat(member.getNickname()).isNotEqualTo(name);
        assertThat(member.getPhone()).isNotEqualTo(phone);
        assertThat(member.getPassword()).isNotEqualTo(password);
        assertThat(member.getBirthYear()).isZero();
        assertThat(member.getBirthMonth()).isZero();
        assertThat(member.getBirthDay()).isZero();
    }
}