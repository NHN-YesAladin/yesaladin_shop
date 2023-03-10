package shop.yesaladin.shop.member.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import shop.yesaladin.common.exception.ClientException;


class MemberAddressTest {

    @Test
    void markAsDefault() {
        //given
        MemberAddress memberAddress = MemberAddress.builder().isDefault(false).build();
        //when
        memberAddress.markAsDefault(true);

        //then
        assertThat(memberAddress.isDefault()).isTrue();
    }

    @Test
    void delete_fail_alreadyDeletedAddress() {
        //given
        MemberAddress memberAddress = MemberAddress.builder().isDeleted(true).build();
        //when,then
        assertThatThrownBy(() -> memberAddress.delete()).isInstanceOf(ClientException.class);
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