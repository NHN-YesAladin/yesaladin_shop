package shop.yesaladin.shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.repository.CommandMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberAddressCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberAddressCommandResponseDto;
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

    @Override
    @Transactional
    public MemberAddressCommandResponseDto save(
            String loginId,
            MemberAddressCreateRequestDto request
    ) {
        Member member = tryGetMemberById(loginId);

        MemberAddress newMemberAddress = request.toEntity(member);

        MemberAddress savedMemberAddress = commandMemberAddressRepository.save(newMemberAddress);

        return MemberAddressCommandResponseDto.fromEntity(savedMemberAddress);
    }

    @Override
    @Transactional
    public MemberAddressCommandResponseDto markAsDefault(String loginId, long addressId) {
        MemberAddress memberAddress = tryGetMemberAddressByMemberIdAndMemberAddressId(
                loginId,
                addressId
        );

        commandMemberAddressRepository.updateIsDefaultToFalseByLoginId(loginId);

        memberAddress.markAsDefault();

        return MemberAddressCommandResponseDto.fromEntity(memberAddress);
    }

    @Override
    @Transactional
    public long delete(String loginId, long addressId) {
        if (!queryMemberAddressRepository.existByLoginIdAndMemberAddressId(loginId, addressId)) {
            throw new MemberAddressNotFoundException(addressId);
        }

        commandMemberAddressRepository.deleteById(addressId);

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
