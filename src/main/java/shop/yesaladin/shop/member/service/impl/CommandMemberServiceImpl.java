package shop.yesaladin.shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.dto.MemberCreateDto;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;

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
    // TODO: CommandGradeService

    @Transactional
    @Override
    public Member create(MemberCreateDto createDto) {

//        return commandMemberRepository.save(member);
        return null;
    }
}
