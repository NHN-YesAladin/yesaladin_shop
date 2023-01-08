package shop.yesaladin.shop.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.repository.QueryMemberGradeRepository;
import shop.yesaladin.shop.member.exception.MemberGradeNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeService;

/**
 * 회원 등급 조회용 서비스 구현체 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryMemberGradeServiceImpl implements QueryMemberGradeService {

    private final QueryMemberGradeRepository queryMemberGradeRepository;

    /**
     * 회원 등급을 조회 하기 위한 메서드 입니다.
     *
     * @param id 회원 등급의 primary key
     * @return 회원의 등급 조회 결과
     * @author : 송학현
     * @since : 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public MemberGrade findById(int id) {
        return queryMemberGradeRepository.findById(id)
                .orElseThrow(() -> new MemberGradeNotFoundException(id));
    }
}
