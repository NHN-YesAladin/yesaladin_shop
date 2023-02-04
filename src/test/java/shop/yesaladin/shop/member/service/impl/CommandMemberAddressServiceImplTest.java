package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.repository.CommandMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberAddressCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.service.inter.CommandMemberAddressService;

class CommandMemberAddressServiceImplTest {

    CommandMemberAddressService commandMemberAddressService;

    QueryMemberRepository queryMemberRepository;
    CommandMemberAddressRepository commandMemberAddressRepository;
    QueryMemberAddressRepository queryMemberAddressRepository;

    String address = "Gwang Ju";
    Boolean isDefault = false;
    String loginId = "user@1";

    Member member = MemberDummy.dummyWithLoginIdAndId(loginId);

    @BeforeEach
    void setUp() {
        queryMemberRepository = Mockito.mock(QueryMemberRepository.class);
        queryMemberAddressRepository = Mockito.mock(QueryMemberAddressRepository.class);
        commandMemberAddressRepository = Mockito.mock(CommandMemberAddressRepository.class);

        commandMemberAddressService = new CommandMemberAddressServiceImpl(
                queryMemberRepository,
                commandMemberAddressRepository,
                queryMemberAddressRepository
        );

    }

    @Test
    void save_failedByMemberNotFound() {
        String loginId = "user@1";

        MemberAddressCreateRequestDto request = ReflectionUtils.newInstance(
                MemberAddressCreateRequestDto.class,
                address,
                isDefault
        );

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(ClientException.class);

        assertThatThrownBy(() -> commandMemberAddressService.save(loginId, request)).isInstanceOf(
                ClientException.class);
    }

    @Test
    void save_failedByAddressRegistrationRestrictionException() {
        //given
        String loginId = "user@1";
        MemberAddressCreateRequestDto request = ReflectionUtils.newInstance(
                MemberAddressCreateRequestDto.class,
                address,
                false
        );

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));
        Mockito.when(queryMemberAddressRepository.countByLoginId(loginId))
                .thenReturn(10L);

        //when, then
        assertThatThrownBy(() -> commandMemberAddressService.save(loginId, request)).isInstanceOf(
                ClientException.class);
    }

    @Test
    void save() {
        String loginId = member.getLoginId();
        long addressId = 1L;

        MemberAddressCreateRequestDto request = ReflectionUtils.newInstance(
                MemberAddressCreateRequestDto.class,
                address,
                true
        );
        MemberAddress memberAddress = getDefaultMemberAddressWithId(addressId, member, false);

        Mockito.when(queryMemberAddressRepository.findByLoginIdAndMemberAddressId(
                loginId,
                addressId
        )).thenReturn(Optional.of(memberAddress));
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));
        Mockito.when(queryMemberAddressRepository.countByLoginId(loginId))
                .thenReturn(1L);
        Mockito.when(commandMemberAddressRepository.save(any())).thenReturn(memberAddress);

        MemberAddressResponseDto actual = commandMemberAddressService.save(loginId, request);

        assertThat(actual.getAddress()).isEqualTo(address);
        assertThat(actual.getIsDefault()).isTrue();

        ArgumentCaptor<MemberAddress> captor = ArgumentCaptor.forClass(MemberAddress.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(anyString());
        verify(commandMemberAddressRepository, times(1)).save(captor.capture());

        MemberAddress capturedMemberAddress = captor.getValue();
        assertThat(capturedMemberAddress.getAddress()).isEqualTo(memberAddress.getAddress());
        assertThat(capturedMemberAddress.getMember().getId()).isEqualTo(memberAddress.getMember()
                .getId());
        assertThat(capturedMemberAddress.getMember()
                .getLoginId()).isEqualTo(memberAddress.getMember().getLoginId());
    }

    @Test
    void markAsDefault_failedByMemberAddressNotFound() {
        long addressId = 1L;

        Mockito.when(queryMemberAddressRepository.getByLoginIdAndMemberAddressId(
                loginId,
                addressId
        )).thenThrow(
                ClientException.class);

        assertThatThrownBy(() -> commandMemberAddressService.markAsDefault(
                loginId,
                addressId
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void markAsDefault() {
        long addressId = 1L;

        MemberAddress memberAddress = getMemberAddressWithId(addressId, member, false);

        Mockito.when(queryMemberAddressRepository.findByLoginIdAndMemberAddressId(
                loginId,
                addressId
        )).thenReturn(Optional.of(memberAddress));

        MemberAddressResponseDto result = commandMemberAddressService.markAsDefault(
                loginId,
                addressId
        );

        assertThat(result.getAddress()).isEqualTo(address);
        assertThat(result.getIsDefault()).isTrue();
    }

    @Test
    void delete_failedByMemberAddressNotFound() {
        String loginId = "user@1";
        long addressId = 1L;

        Mockito.when(queryMemberAddressRepository.findByLoginIdAndMemberAddressId(
                loginId,
                addressId
        )).thenThrow(new ClientException(ErrorCode.ADDRESS_NOT_FOUND, "address not found"));

        assertThatThrownBy(() -> commandMemberAddressService.delete(
                loginId,
                addressId
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void delete_failedByAlreadyDeletedAddress() {
        long addressId = 1L;

        MemberAddress memberAddress = getMemberAddress(member, true);

        Mockito.when(queryMemberAddressRepository.findByLoginIdAndMemberAddressId(
                loginId,
                addressId
        )).thenReturn(Optional.of(memberAddress));

        assertThatThrownBy(() -> commandMemberAddressService.delete(
                loginId,
                addressId
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void delete_success() {
        long addressId = 1L;

        MemberAddress memberAddress = getMemberAddressWithId(addressId, member, false);
        Mockito.when(queryMemberAddressRepository.findByLoginIdAndMemberAddressId(
                loginId,
                addressId
        )).thenReturn(Optional.of(memberAddress));

        long result = commandMemberAddressService.delete(loginId, addressId);

        assertThat(result).isEqualTo(addressId);
        assertThat(memberAddress.isDeleted()).isTrue();
    }

    private MemberAddress getMemberAddress(Member member, boolean isDeleted) {
        return MemberAddress.builder()
                .address(address)
                .isDefault(isDefault)
                .isDeleted(isDeleted)
                .member(member)
                .build();
    }

    private MemberAddress getMemberAddressWithId(long addressId, Member member, boolean isDeleted) {
        return MemberAddress.builder()
                .id(addressId)
                .address(address)
                .isDefault(isDefault)
                .isDeleted(isDeleted)
                .member(member)
                .build();
    }
    private MemberAddress getDefaultMemberAddressWithId(long addressId, Member member, boolean isDeleted) {
        return MemberAddress.builder()
                .id(addressId)
                .address(address)
                .isDefault(true)
                .isDeleted(isDeleted)
                .member(member)
                .build();
    }
}