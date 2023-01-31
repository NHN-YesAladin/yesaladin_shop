package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;

class QueryMemberAddressServiceImplTest {

    QueryMemberAddressService queryMemberAddressService;
    QueryMemberRepository queryMemberRepository;
    QueryMemberAddressRepository queryMemberAddressRepository;


    String address = "Gwang Ju";
    Boolean isDefault = false;

    @BeforeEach
    void setUp() {
        queryMemberRepository = Mockito.mock(QueryMemberRepository.class);
        queryMemberAddressRepository = Mockito.mock(QueryMemberAddressRepository.class);

        queryMemberAddressService = new QueryMemberAddressServiceImpl(
                queryMemberRepository,
                queryMemberAddressRepository
        );
    }

    @Test
    void findByLoginId_failedByMemberNotFound() {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(MemberNotFoundException.class);

        //when,then
        assertThatThrownBy(() -> queryMemberAddressService.findByLoginId(loginId)).isInstanceOf(
                MemberNotFoundException.class);
    }

    @Test
    void findByLoginId() {
        //given
        String loginId = "user@1";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        Mockito.when(queryMemberAddressRepository.findByLoginId(member))
                .thenReturn(getMemberAddressList(10, member));
        //when
        List<MemberAddressResponseDto> result = queryMemberAddressService.findByLoginId(loginId);

        //then
        assertThat(result).hasSize(10);
        assertThat(result.stream()
                .filter(x -> !Objects.equals(x.getLoginId(), loginId))
                .findFirst()).isEmpty();
    }

    List<MemberAddress> getMemberAddressList(int cnt, Member member) {
        List<MemberAddress> memberAddressList = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            memberAddressList.add(MemberAddress.builder()
                    .id((long) i + 1)
                    .address(address)
                    .isDefault(isDefault)
                    .member(member)
                    .build());
        }
        return memberAddressList;
    }
}