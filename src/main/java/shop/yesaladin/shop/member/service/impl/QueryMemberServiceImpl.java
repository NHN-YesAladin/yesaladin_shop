package shop.yesaladin.shop.member.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRoleRepository;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원 조회용 서비스 구현체 입니다.
 *
 * @author 송학현
 * @author 최예린
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QueryMemberServiceImpl implements QueryMemberService {

    private final QueryMemberRepository queryMemberRepository;
    private final QueryMemberRoleRepository queryMemberRoleRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberById(long id) {
        Member member = queryMemberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Id: " + id));
        return MemberDto.fromEntity(member);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberByLoginId(String loginId) {
        Member member = getMemberByLoginId(loginId,
                queryMemberRepository.findMemberByLoginId(loginId),
                "Member Login Id: "
        );
        return MemberDto.fromEntity(member);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberByNickname(String nickname) {
        Member member = queryMemberRepository.findMemberByNickname(nickname)
                .orElseThrow(() -> new MemberNotFoundException("Member Nickname: " + nickname));
        return MemberDto.fromEntity(member);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberLoginResponseDto findMemberLoginInfoByLoginId(String loginId) {
        Member member = getMemberByLoginId(loginId,
                queryMemberRepository.findMemberByLoginId(loginId),
                "Member Login Id: "
        );

        List<String> roles = queryMemberRoleRepository.findMemberRolesByMemberId(member.getId());

        return new MemberLoginResponseDto(member.getId(),
                member.getName(),
                member.getNickname(),
                member.getLoginId(),
                member.getEmail(),
                member.getPassword(),
                roles
        );
    }

    @Override
    public List<Member> findMembersByBirthday(int laterDays) {
        LocalDate birthday = LocalDate.now().plusDays(laterDays);
        log.info("birthday : {}", birthday);

        return queryMemberRepository.findMembersByBirthday(birthday.getMonthValue(),
                birthday.getDayOfMonth()
        );
    }

    private Member getMemberByLoginId(String loginId, Optional<Member> memberByLoginId, String s) {
        return memberByLoginId.orElseThrow(() -> new MemberNotFoundException(s + loginId));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsLoginId(String loginId) {
        return queryMemberRepository.existsMemberByLoginId(loginId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsNickname(String nickname) {
        return queryMemberRepository.existsMemberByNickname(nickname);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsEmail(String email) {
        return queryMemberRepository.existsMemberByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsPhone(String phone) {
        return queryMemberRepository.existsMemberByPhone(phone);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberGradeQueryResponseDto getMemberGrade(String loginId) {
        return MemberGradeQueryResponseDto.fromEntity(queryMemberRepository.findMemberByLoginId(
                        loginId)
                .orElseThrow(() -> new MemberNotFoundException("Member Loginid : " + loginId)));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberQueryResponseDto getByLoginId(String loginId) {
        return MemberQueryResponseDto.fromEntity(queryMemberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> new MemberNotFoundException("Member Loginid : " + loginId)));
    }
}
