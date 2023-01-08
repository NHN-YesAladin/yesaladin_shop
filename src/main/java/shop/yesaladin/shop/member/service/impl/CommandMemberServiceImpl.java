package shop.yesaladin.shop.member.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberCreateRequest;
import shop.yesaladin.shop.member.dto.MemberCreateResponse;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeService;

/**
 * 회원 등록/수정/삭제용 서비스 구현체 입니다.
 *
 * @author : 송학현
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

    private void checkUniqueData(Optional<Member> member, String target) {
        if (member.isPresent()) {
            throw new MemberProfileAlreadyExistException(target);
        }
    }
}
