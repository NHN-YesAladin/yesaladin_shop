package shop.yesaladin.shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.repository.CommandMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.dto.MemberAddressCreateRequestDto;
import shop.yesaladin.shop.member.exception.AlreadyRegisteredUpToLimit;
import shop.yesaladin.shop.member.exception.MemberAddressNotFoundException;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.CommandMemberAddressService;

/**
 * 회원 배송지 등록/수정/삭제 전용 서비스 구현체 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandMemberAddressServiceImpl implements CommandMemberAddressService {

    private final QueryMemberRepository queryMemberRepository;
    private final CommandMemberAddressRepository commandMemberAddressRepository;
    private final QueryMemberAddressRepository queryMemberAddressRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MemberAddressResponseDto save(
            String loginId,
            MemberAddressCreateRequestDto request
    ) {
        Member member = tryGetMemberById(loginId);
        checkMemberAddressCountLimitByLoginId(loginId);

        MemberAddress newMemberAddress = request.toEntity(member);
        if (newMemberAddress.isDefault()) {
            commandMemberAddressRepository.updateIsDefaultToFalseByLoginId(loginId);
        }

        MemberAddress savedMemberAddress = commandMemberAddressRepository.save(newMemberAddress);

        return MemberAddressResponseDto.fromEntity(savedMemberAddress);
    }

    private void checkMemberAddressCountLimitByLoginId(String loginId) {
        if (queryMemberAddressRepository.countByLoginId(loginId) == 10) {
            throw new AlreadyRegisteredUpToLimit(loginId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MemberAddressResponseDto markAsDefault(String loginId, long addressId) {
        MemberAddress memberAddress = tryGetMemberAddressByMemberIdAndMemberAddressId(
                loginId,
                addressId
        );

        commandMemberAddressRepository.updateIsDefaultToFalseByLoginId(loginId);

        memberAddress.markAsDefault();

        return MemberAddressResponseDto.fromEntity(memberAddress);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public long delete(String loginId, long addressId) {
        MemberAddress memberAddress = tryGetMemberAddressByMemberIdAndMemberAddressId(
                loginId,
                addressId
        );

        memberAddress.delete();

        return addressId;
    }

    private MemberAddress tryGetMemberAddressByMemberIdAndMemberAddressId(
            String loginId,
            long addressId
    ) {
        return queryMemberAddressRepository.getByLoginIdAndMemberAddressId(
                loginId,
                addressId
        ).orElseThrow(() -> new MemberAddressNotFoundException(addressId));
    }


    private Member tryGetMemberById(String loginId) {
        return queryMemberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> new MemberNotFoundException("Member Id: " + loginId));
    }
}
