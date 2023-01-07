package shop.yesaladin.shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원 조회용 서비스 구현체 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryMemberServiceImpl implements QueryMemberService {

    private final QueryMemberRepository queryMemberRepository;

    /**
     * 회원을 조회 하기 위한 메서드 입니다.
     *
     * @param id member의 primary key
     * @return 회원 조회 결과
     */
    @Transactional(readOnly = true)
    @Override
    public Member findMemberById(long id) {
        return queryMemberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
}
