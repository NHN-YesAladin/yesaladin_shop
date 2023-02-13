package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;

class QueryMemberAddressServiceImplTest {

    QueryMemberAddressService queryMemberAddressService;
    QueryMemberRepository queryMemberRepository;
    QueryMemberAddressRepository queryMemberAddressRepository;


    String address = "서울특별시 구로구 디지털로26길 72 (구로동, NHN KCP)";
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
    void getByLoginId_failedByMemberNotFound() {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberRepository.existsMemberByLoginId(loginId))
                .thenReturn(false);

        //when,then
        assertThatThrownBy(() -> queryMemberAddressService.getByLoginId(loginId)).isInstanceOf(
                ClientException.class);
    }

    @Test
    void getByLoginId() {
        //given
        String loginId = "user@1";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);

        Mockito.when(queryMemberRepository.existsMemberByLoginId(loginId))
                .thenReturn(true);

        Mockito.when(queryMemberAddressRepository.getByLoginId(loginId))
                .thenReturn(getMemberAddressList(member));
        //when
        List<MemberAddressResponseDto> result = queryMemberAddressService.getByLoginId(loginId);

        //then
        assertThat(result).hasSize(10);
    }

    List<MemberAddressResponseDto> getMemberAddressList(Member member) {
        List<MemberAddressResponseDto> memberAddressList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            memberAddressList.add(MemberAddressResponseDto.fromEntity(MemberAddress.builder()
                    .id((long) i + 1)
                    .address(address)
                    .isDefault(isDefault)
                    .member(member)
                    .build()));
        }
        return memberAddressList;
    }
}