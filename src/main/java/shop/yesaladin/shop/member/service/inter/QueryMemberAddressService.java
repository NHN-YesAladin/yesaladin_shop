package shop.yesaladin.shop.member.service.inter;

import java.util.List;
import shop.yesaladin.shop.member.dto.MemberAddressQueryDto;

/**
 * 회원배송지 조회 관련 service interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryMemberAddressService {

    /**
     * 회원의 배송지 목록들을 조회합니다.
     *
     * @param loginId 회원 아이디
     * @return 회원의 배송지 목록
     * @author 최예린
     * @since 1.0
     */
    List<MemberAddressQueryDto> findByLoginId(String loginId);
}
