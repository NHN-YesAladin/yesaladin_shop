package shop.yesaladin.shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원 조회용 서비스 구현체 입니다.
 *
 * @author : 송학현, 최예린
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryMemberServiceImpl implements QueryMemberService {

    private final QueryMemberRepository queryMemberRepository;

    /**
     * 회원을 primary key로 조회 하기 위한 메서드 입니다.
     *
     * @param id member의 primary key
     * @return 회원 조회 결과
     * @author : 송학현, 최예린
     * @since : 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberById(long id) {
        Member member = queryMemberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Id: " + id));
        return MemberDto.fromEntity(member);
    }

    /**
     * 회원을 unique column인 loginId를 기준 으로 조회 하기 위한 메서드 입니다.
     *
     * @param loginId member의 loginId
     * @return 회원 조회 결과
     * @author : 송학현, 최예린
     * @since : 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberByLoginId(String loginId) {
        Member member = queryMemberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> new MemberNotFoundException("Member Login Id: " + loginId));
        return MemberDto.fromEntity(member);
    }

    /**
     * 회원을 unique column인 nickname을 기준 으로 조회 하기 위한 메서드 입니다.
     *
     * @param nickname member의 nickname
     * @return 회원 조회 결과
     * @author : 송학현, 최예린
     * @since : 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberByNickname(String nickname) {
        Member member = queryMemberRepository.findMemberByNickname(nickname)
                .orElseThrow(() -> new MemberNotFoundException("Member Nickname: " + nickname));
        return MemberDto.fromEntity(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberLoginResponseDto findMemberLoginInfoByLoginId(String loginId) {
        return null;
    }
}
