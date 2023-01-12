package shop.yesaladin.shop.member.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


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
}