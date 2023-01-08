package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.dto.MemberCreateRequest;
import shop.yesaladin.shop.member.dto.MemberCreateResponse;

/**
 * Create, Update, Delete 를 Controller Layer에서 사용하기 위한 service interface
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface CommandMemberService {

    /**
     * 회원을 등록하기 위한 기능 입니다.
     *
     * @param createDto 회원 등록 요청 dto
     * @return 회원 등록 결과를 반환할 dto
     * @author : 송학현
     * @since : 1.0
     */
    MemberCreateResponse create(MemberCreateRequest createDto);
}
