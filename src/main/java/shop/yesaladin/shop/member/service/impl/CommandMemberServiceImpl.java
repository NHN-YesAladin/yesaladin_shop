package shop.yesaladin.shop.member.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberBlockResponse;
import shop.yesaladin.shop.member.dto.MemberCreateRequest;
import shop.yesaladin.shop.member.dto.MemberCreateResponse;
import shop.yesaladin.shop.member.dto.MemberUpdateRequest;
import shop.yesaladin.shop.member.dto.MemberUpdateResponse;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeService;

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
    private final QueryMemberGradeService queryMemberGradeService;

    private static final int WHITE_GRADE_ID = 1;

    /**
     * 회원 등록을 위한 기능 입니다.
     *
     * @param createDto 회원 등록 요청 dto
     * @return 등록된 회원 결과 dto
     * @author : 송학현
     * @since : 1.0
     */
    @Transactional
    @Override
    public MemberCreateResponse create(MemberCreateRequest createDto) {
        checkUniqueData(queryMemberRepository.findMemberByLoginId(createDto.getLoginId()), "id");
        checkUniqueData(
                queryMemberRepository.findMemberByNickname(createDto.getNickname()),
                "nickname"
        );

        MemberGrade memberGrade = queryMemberGradeService.findById(WHITE_GRADE_ID);
        Member member = createDto.toEntity(memberGrade);
        Member savedMember = commandMemberRepository.save(member);

        return MemberCreateResponse.fromEntity(savedMember);
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
    public MemberUpdateResponse update(Long id, MemberUpdateRequest updateDto) {
        Member member = queryMemberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Id: " + id));

        checkUniqueData(
                queryMemberRepository.findMemberByNickname(updateDto.getNickname()),
                "nickname"
        );

        member.setNewNickname(updateDto.getNickname());
        Member updatedMember = commandMemberRepository.save(member);

        return MemberUpdateResponse.fromEntity(updatedMember);
    }

    /**
     * 회원 차단/해지를 위한 기능 입니다.
     *
     * @param id      차단/해지할 회원 id
     * @param blocked 차단(ture)/해지(false)
     * @author 최예린
     * @since 1.0
     */
    @Transactional
    @Override
    public MemberBlockResponse updateBlocked(Long id, boolean blocked) {
        Member member = queryMemberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Id: " + id));

        member.setMemberBlocked(blocked);
        Member updatedMember = commandMemberRepository.save(member);

        return MemberBlockResponse.fromEntity(updatedMember);
    }

    private void checkUniqueData(Optional<Member> member, String target) {
        if (member.isPresent()) {
            throw new MemberProfileAlreadyExistException(target);
        }
    }
}
