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
import shop.yesaladin.shop.member.dto.MemberAddressCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberAddressUpdateResponseDto;
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
    public MemberAddressCreateResponseDto save(
            Long memberId,
            MemberAddressCreateRequestDto request
    ) {
        Member member = tryGetMemberById(memberId);

        MemberAddress newMemberAddress = request.toEntity(member);

        MemberAddress savedMemberAddress = commandMemberAddressRepository.save(newMemberAddress);

        return MemberAddressCreateResponseDto.fromEntity(savedMemberAddress);
    }

    @Override
    @Transactional
    public MemberAddressUpdateResponseDto markAsDefault(Long memberId, Long addressId) {
        MemberAddress memberAddress = tryGetMemberAddressByMemberIdAndMemberAddressId(
                memberId,
                addressId
        );

        commandMemberAddressRepository.updateIsDefaultToFalseByMemberId(memberId);

        memberAddress.markAsDefault();

        return MemberAddressUpdateResponseDto.fromEntity(memberAddress);
    }

    @Override
    @Transactional
    public long delete(Long memberId, Long addressId) {
        if (!queryMemberAddressRepository.existByMemberIdAndMemberAddressId(memberId, addressId)) {
            throw new MemberAddressNotFoundException(addressId);
        }

        commandMemberAddressRepository.deleteById(addressId);

        return addressId;
    }

    private MemberAddress tryGetMemberAddressByMemberIdAndMemberAddressId(
            Long memberId,
            Long addressId
    ) {
        return queryMemberAddressRepository.getByMemberIdAndMemberAddressId(
                memberId,
                addressId
        ).orElseThrow(() -> new MemberAddressNotFoundException(addressId));
    }


    private Member tryGetMemberById(Long memberId) {
        return queryMemberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member Id: " + memberId));
    }
}
