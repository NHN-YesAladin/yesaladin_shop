package shop.yesaladin.shop.member.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.member.exception.AlreadyBlockedMemberException;
import shop.yesaladin.shop.member.exception.AlreadyUnblockedMemberException;

class MemberTest {

    @Test
    void unblockMember_failedWithAlreadyUnblockedMember() {
        //given
        Member blockedMember = Member.builder().id(1L).isBlocked(false).build();

        //when, then
        assertThatThrownBy(blockedMember::unblockMember).isInstanceOf(
                AlreadyUnblockedMemberException.class);
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
        Member unblockedMember = Member.builder().id(1L).isBlocked(true).build();

        //when, then
        assertThatThrownBy(unblockedMember::blockMember).isInstanceOf(
                AlreadyBlockedMemberException.class);

    }

    @Test
    void blockMember() {
        //given
        Member unblockedMember = Member.builder().id(1L).isBlocked(false).build();

        //when
        unblockedMember.blockMember();

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
}