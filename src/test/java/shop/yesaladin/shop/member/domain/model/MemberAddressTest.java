package shop.yesaladin.shop.member.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.member.exception.AlreadyDeletedAddressException;


class MemberAddressTest {

    @Test
    void markAsDefault() {
        //given
        MemberAddress memberAddress = MemberAddress.builder().isDefault(false).build();
        //when
        memberAddress.markAsDefault();

        //then
        assertThat(memberAddress.isDefault()).isTrue();
    }

    @Test
    void delete_fail_alreadyDeletedAddress() {
        //given
        MemberAddress memberAddress = MemberAddress.builder().isDeleted(true).build();
        //when,then
        assertThatThrownBy(() -> memberAddress.delete()).isInstanceOf(AlreadyDeletedAddressException.class);
    }

    @Test
    void delete() {
        //given
        MemberAddress memberAddress = MemberAddress.builder().isDeleted(false).build();
        //when
        memberAddress.delete();

        //then
        assertThat(memberAddress.isDeleted()).isTrue();
    }
}