package shop.yesaladin.shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.MemberRole.Pk;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRoleRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryRoleRepository;
import shop.yesaladin.shop.member.dto.MemberBlockRequestDto;
import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberUnblockResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberUpdateResponseDto;
import shop.yesaladin.shop.member.dto.MemberWithdrawResponseDto;
import shop.yesaladin.shop.member.event.SignUpEvent;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;
import shop.yesaladin.shop.member.exception.MemberRoleNotFoundException;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;

/**
 * 회원 등록/수정/삭제용 서비스 구현체 입니다.
 *
 * @author 송학현
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandMemberServiceImpl implements CommandMemberService {

    private final CommandMemberRepository commandMemberRepository;
    private final QueryMemberRepository queryMemberRepository;
    private final QueryRoleRepository queryRoleRepository;
    private final CommandMemberRoleRepository commandMemberRoleRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public MemberCreateResponseDto create(MemberCreateRequestDto createDto) {
        int roleId = 1;

        Role roleMember = queryRoleRepository.findById(roleId).orElseThrow(
                () -> new MemberRoleNotFoundException(roleId));

        checkMemberProfileExist(createDto);

        Member member = createDto.toEntity();
        Member savedMember = commandMemberRepository.save(member);

        MemberRole memberRole = createMemberRole(
                savedMember,
                roleId,
                roleMember
        );

        commandMemberRoleRepository.save(memberRole);

        eventPublisher.publishEvent(new SignUpEvent(this, member.getLoginId()));

        return MemberCreateResponseDto.fromEntity(savedMember, roleMember);
    }

    /**
     * 중복 사항을 체크하는 메소드 입니다.
     *
     * @param createDto 회원 등록 요청 dto
     * @throws MemberProfileAlreadyExistException loginId, nickname, email이 기존에 있다면 발생하는 예외입니다.
     * @author 송학현
     * @since 1.0
     */
    private void checkMemberProfileExist(MemberCreateRequestDto createDto) {
        if (queryMemberRepository.existsMemberByLoginId(createDto.getLoginId())) {
            throw new MemberProfileAlreadyExistException(createDto.getLoginId());
        }

        if (queryMemberRepository.existsMemberByNickname(createDto.getNickname())) {
            throw new MemberProfileAlreadyExistException(createDto.getNickname());
        }

        if (queryMemberRepository.existsMemberByEmail(createDto.getEmail())) {
            throw new MemberProfileAlreadyExistException(createDto.getEmail());
        }

        if (queryMemberRepository.existsMemberByPhone(createDto.getPhone())) {
            throw new MemberProfileAlreadyExistException(createDto.getPhone());
        }
    }

    private MemberRole createMemberRole(Member savedMember, int roleId, Role roleMember) {
        return MemberRole.builder()
                .id(new Pk(savedMember.getId(), roleId))
                .member(savedMember)
                .role(roleMember)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public MemberUpdateResponseDto update(String loginId, MemberUpdateRequestDto request) {
        Member member = tryGetMemberById(loginId);

        checkNewNicknameIsUnique(request);

        member.changeNickname(request.getNickname());

        return MemberUpdateResponseDto.fromEntity(member);
    }

    private void checkNewNicknameIsUnique(MemberUpdateRequestDto request) {
        if (queryMemberRepository.findMemberByNickname(request.getNickname()).isPresent()) {
            throw new ClientException(
                    ErrorCode.MEMBER_NICKNAME_ALREADY_EXIST,
                    "Member nickname is already exist with nickname : "
                            + request.getNickname()
                            + "."
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public MemberBlockResponseDto block(String loginId, MemberBlockRequestDto request) {
        Member member = tryGetMemberById(loginId);

        member.blockMember(request.getBlockedReason());

        return MemberBlockResponseDto.fromEntity(member);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public MemberUnblockResponseDto unblock(String loginId) {
        Member member = tryGetMemberById(loginId);

        member.unblockMember();

        return MemberUnblockResponseDto.fromEntity(member);
    }

    private Member tryGetMemberById(String loginId) {
        return queryMemberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member not found with loginId : " + loginId
                ));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public MemberWithdrawResponseDto withDraw(String loginId) {
        Member member = queryMemberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> new MemberNotFoundException("Member loginId: " + loginId));

        member.withdrawMember();

        return MemberWithdrawResponseDto.fromEntity(member);
    }
}
