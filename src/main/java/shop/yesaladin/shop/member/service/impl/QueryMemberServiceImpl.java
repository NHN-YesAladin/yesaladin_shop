package shop.yesaladin.shop.member.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRoleRepository;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberIdDto;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerListResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.dto.OrderSheetResponseDto;

/**
 * 회원 조회용 서비스 구현체 입니다.
 *
 * @author 송학현
 * @author 최예린
 * @author 서민지
 * @author 김선홍
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
    @Override
    @Transactional(readOnly = true)
    public Member findByLoginId(String loginId) {
        return queryMemberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member not found with loginId : " + loginId
                ));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberByLoginId(String loginId) {
        Member member = getMemberByLoginId(
                loginId,
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
        Member member = getMemberByLoginId(
                loginId,
                queryMemberRepository.findMemberByLoginId(loginId),
                "Member Login Id: "
        );

        List<String> roles = queryMemberRoleRepository.findMemberRolesByMemberId(member.getId());

        return new MemberLoginResponseDto(
                member.getId(),
                member.getName(),
                member.getNickname(),
                member.getLoginId(),
                member.getEmail(),
                member.getPassword(),
                roles
        );
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberManagerResponseDto findMemberManageByLoginId(String loginId) {
        return MemberManagerResponseDto.fromEntity(queryMemberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> {
                    throw new MemberNotFoundException("Member LoginId : " + loginId);
                }));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberManagerResponseDto findMemberManageByNickName(String nickname) {
        return MemberManagerResponseDto.fromEntity(queryMemberRepository.findMemberByNickname(
                nickname).orElseThrow(() -> {
            throw new MemberNotFoundException("Member Nickname : " + nickname);
        }));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberManagerResponseDto findMemberManageByPhone(String phone) {
        return MemberManagerResponseDto.fromEntity(queryMemberRepository.findMemberByPhone(
                phone).orElseThrow(() -> {
            throw new MemberNotFoundException("Member Phone : " + phone);
        }));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberManagerListResponseDto findMemberManagesByName(
            String name,
            int offset,
            int limit
    ) {
        Page<Member> result = queryMemberRepository.findMembersByName(name, offset, limit);
        return MemberManagerListResponseDto.builder()
                .count(result.getTotalElements())
                .memberManagerResponseDtoList(Optional.of(queryMemberRepository.findMembersByName(
                                name,
                                offset,
                                limit
                        ).getContent())
                        .filter(m -> !m.isEmpty())
                        .orElseThrow(() -> new MemberNotFoundException(""))
                        .stream()
                        .map(MemberManagerResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberManagerListResponseDto findMemberManagesBySignUpDate(
            LocalDate signUpDate,
            int offset,
            int limit
    ) {
        Page<Member> result = queryMemberRepository.findMembersBySignUpDate(
                signUpDate,
                offset,
                limit
        );
        return MemberManagerListResponseDto.builder()
                .count(result.getTotalElements())
                .memberManagerResponseDtoList(Optional.of(queryMemberRepository.findMembersBySignUpDate(
                                signUpDate,
                                offset,
                                limit
                        ).getContent())
                        .filter(m -> !m.isEmpty())
                        .orElseThrow(() -> new MemberNotFoundException(""))
                        .stream()
                        .map(MemberManagerResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<MemberIdDto> findMemberIdsByBirthday(int laterDays) {
        LocalDate birthday = LocalDate.now().plusDays(laterDays);
        return queryMemberRepository.findMemberIdsByBirthday(
                birthday.getMonthValue(),
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
    public MemberGradeQueryResponseDto getMemberGradeByLoginId(String loginId) {
        return MemberGradeQueryResponseDto.fromEntity(queryMemberRepository.findMemberByLoginId(
                        loginId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member not found with loginId : " + loginId
                )));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberQueryResponseDto getByLoginId(String loginId) {
        return MemberQueryResponseDto.fromEntity(queryMemberRepository.findMemberByLoginId(
                        loginId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member not found with loginId : " + loginId
                )));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OrderSheetResponseDto getMemberForOrder(String loginId) {
        return queryMemberRepository.getMemberOrderData(loginId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member not found with loginId : " + loginId
                ));
    }
}
