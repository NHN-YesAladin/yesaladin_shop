package shop.yesaladin.shop.member.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;

/**
 * 회원배송지 조회 관련 service 구현체입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryMemberAddressServiceImpl implements QueryMemberAddressService {

    private final QueryMemberRepository queryMemberRepository;
    private final QueryMemberAddressRepository queryMemberAddressRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MemberAddressResponseDto> findByLoginId(String loginId) {
        Member member = tryGetMemberById(loginId);

        return queryMemberAddressRepository.findByLoginId(member)
                .stream()
                .map(MemberAddressResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    private Member tryGetMemberById(String loginId) {
        return queryMemberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> new MemberNotFoundException("Member loginId: " + loginId));
    }
}
