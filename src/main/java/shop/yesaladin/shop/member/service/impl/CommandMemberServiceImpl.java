package shop.yesaladin.shop.member.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.MemberRole.Pk;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRoleRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryRoleRepository;
import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberUpdateResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;

/**
 * 회원 등록/수정/삭제용 서비스 구현체 입니다.
 *
 * @author : 송학현, 최예린
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandMemberServiceImpl implements CommandMemberService {

    private final CommandMemberRepository commandMemberRepository;
    private final QueryMemberRepository queryMemberRepository;
    private final QueryRoleRepository queryRoleRepository;
    private final CommandMemberRoleRepository commandMemberRoleRepository;

    /**
     * 회원 등록을 위한 기능 입니다.
     * 회원 등록시 ROLE_MEMBER 권한과 WHITE 회원 등급을 함께 등록합니다.
     *
     * @param createDto 회원 등록 요청 dto
     * @return 등록된 회원 결과 dto
     * @author : 송학현
     * @since : 1.0
     */
    @Transactional
    @Override
    public MemberCreateResponseDto create(MemberCreateRequestDto createDto) {
        checkUniqueData(queryMemberRepository.findMemberByLoginId(createDto.getLoginId()), "id");
        checkUniqueData(
                queryMemberRepository.findMemberByNickname(createDto.getNickname()),
                "nickname"
        );

        Member member = createDto.toEntity();
        Member savedMember = commandMemberRepository.save(member);

        int roleId = 1;

        Role roleMember = queryRoleRepository.findById(roleId).orElseThrow(RuntimeException::new);

        MemberRole memberRole = createMemberRole(
                savedMember,
                roleId,
                roleMember
        );

        commandMemberRoleRepository.save(memberRole);

        return MemberCreateResponseDto.fromEntity(savedMember, roleMember);
    }

    private MemberRole createMemberRole(Member savedMember, int roleId, Role roleMember) {
        return MemberRole.builder()
                .id(new Pk(savedMember.getId(), roleId))
                .member(savedMember)
                .role(roleMember)
                .build();
    }

    /**
     * 회원 정보 수정을 위한 기능입니다.
     *
     * @param id        정보를 수정한 회원 id
     * @param updateDto 수정한 회원 정보 dto
     * @return 수정된 결과를 반환할 dto
     * @author 최예린
     * @since 1.0
     */
    @Transactional
    @Override
    public MemberUpdateResponseDto update(Long id, MemberUpdateRequestDto updateDto) {
        Member member = tryGetMemberById(id);

        checkUniqueData(
                queryMemberRepository.findMemberByNickname(updateDto.getNickname()),
                "nickname"
        );
        member.changeNickname(updateDto.getNickname());

        return MemberUpdateResponseDto.fromEntity(member);
    }

    /**
     * 회원 차단을 위한 기능 입니다.
     *
     * @param id 차단할 회원 id
     * @author 최예린
     * @since 1.0
     */
    @Transactional
    @Override
    public MemberBlockResponseDto block(Long id) {
        Member member = tryGetMemberById(id);

        member.blockMember();

        return MemberBlockResponseDto.fromEntity(member);
    }

    /**
     * 회원 차단해지를 위한 기능 입니다.
     *
     * @param id 차단해지할 회원 id
     * @author 최예린
     * @since 1.0
     */
    @Transactional
    @Override
    public MemberBlockResponseDto unblock(Long id) {
        Member member = tryGetMemberById(id);

        member.unblockMember();

        return MemberBlockResponseDto.fromEntity(member);
    }

    private Member tryGetMemberById(Long id) {
        return queryMemberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Id: " + id));
    }

    private void checkUniqueData(Optional<Member> member, String target) {
        if (member.isPresent()) {
            throw new MemberProfileAlreadyExistException(target);
        }
    }
}
