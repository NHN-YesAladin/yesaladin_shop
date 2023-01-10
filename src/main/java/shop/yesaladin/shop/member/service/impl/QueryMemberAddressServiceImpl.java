package shop.yesaladin.shop.member.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberAddressQueryDto;
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

    @Override
    @Transactional
    public List<MemberAddressQueryDto> findByMemberId(Long memberId) {
        Member member = tryGetMemberById(memberId);

        return queryMemberAddressRepository.findByMember(member)
                .stream()
                .map(MemberAddressQueryDto::fromEntity)
                .collect(Collectors.toList());
    }

    private Member tryGetMemberById(Long memberId) {
        return queryMemberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member Not Found: " + memberId));
    }
}
