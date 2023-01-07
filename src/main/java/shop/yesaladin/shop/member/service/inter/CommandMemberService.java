package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberCreateRequest;

/**
 * Create, Update, Delete 를 Controller Layer에서 사용하기 위한 service interface
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface CommandMemberService {

    Member create(MemberCreateRequest createDto);
}
