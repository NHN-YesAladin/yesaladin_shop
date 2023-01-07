package shop.yesaladin.shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberCreateDto;
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

    /**
     * 회원 등록을 위한 기능 입니다.
     *
     * @param createDto 회원 등록 요청 dto
     * @return 등록된 회원
     * @author : 송학현
     * @since : 1.0
     */
    @Transactional
    @Override
    public Member create(MemberCreateDto createDto) {
        if (queryMemberRepository.findMemberByLoginId(createDto.getLoginId()).isPresent()) {
            throw new MemberProfileAlreadyExistException("id");
        }

        if (queryMemberRepository.findMemberByNickname(createDto.getNickname()).isPresent()) {
            throw new MemberProfileAlreadyExistException("nickname");
        }

        MemberGrade memberGrade = queryMemberGradeService.findById(1);

        Member member = createDto.toEntity(memberGrade);

        return commandMemberRepository.save(member);
    }
}
