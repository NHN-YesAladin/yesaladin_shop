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

    private static final int WHITE_GRADE_ID = 1;
    private final CommandMemberRepository commandMemberRepository;
    private final QueryMemberRepository queryMemberRepository;
    private final QueryMemberGradeService queryMemberGradeService;

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
        Member member = getInvalidMember(id);

        checkUniqueData(
                queryMemberRepository.findMemberByNickname(updateDto.getNickname()),
                "nickname"
        );
        member.changeNickname(updateDto.getNickname());

        return MemberUpdateResponse.fromEntity(member);
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
    public MemberBlockResponse block(Long id) {
        Member member = getInvalidMember(id);

        member.blockMember();

        return MemberBlockResponse.fromEntity(member);
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
    public MemberBlockResponse unblock(Long id) {
        Member member = getInvalidMember(id);

        member.unblockMember();

        return MemberBlockResponse.fromEntity(member);
    }

    private Member getInvalidMember(Long id) {
        return queryMemberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Id: " + id));
    }

    private void checkUniqueData(Optional<Member> member, String target) {
        if (member.isPresent()) {
            throw new MemberProfileAlreadyExistException(target);
        }
    }
}
