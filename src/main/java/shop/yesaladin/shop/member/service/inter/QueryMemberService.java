package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.domain.model.Member;


/**
 * 회원 조회용 service interface
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface QueryMemberService {

    Member findMemberById(long id);
}
