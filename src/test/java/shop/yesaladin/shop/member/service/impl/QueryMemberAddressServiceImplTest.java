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
import shop.yesaladin.shop.member.dto.MemberAddressQueryDto;
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
    void findByMemberId_failedByMemberNotFound() {
        //given
        Long memberId = 1L;
        Member member = MemberDummy.dummyWithId(memberId);

        Mockito.when(queryMemberRepository.findById(memberId))
                .thenThrow(MemberNotFoundException.class);
        //when,then
        assertThatThrownBy(() -> queryMemberAddressService.findByMemberId(memberId)).isInstanceOf(
                MemberNotFoundException.class);
    }

    @Test
    void findByMemberId() {
        //given
        Long memberId = 1L;
        Member member = MemberDummy.dummyWithId(memberId);

        Mockito.when(queryMemberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        Mockito.when(queryMemberAddressRepository.findByMember(member))
                .thenReturn(getMemberAddressList(10, member));
        //when
        List<MemberAddressQueryDto> result = queryMemberAddressService.findByMemberId(memberId);

        //then
        assertThat(result).hasSize(10);
        assertThat(result.stream()
                .filter(x -> !Objects.equals(x.getMember().getId(), memberId))
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