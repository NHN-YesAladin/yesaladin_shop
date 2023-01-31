package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.order.dto.MemberOrderResponseDto;

/**
 * querydsl용 회원 조회 repository interface 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CustomQueryMemberRepository {

    /**
     * 회원의 아이디로 주문서에 필요한 회원의 데이터를 조회합니다.
     *
     * @param loginId 회원 아이디
     * @return 주문서에 필요한 데이터
     * @author 최예린
     * @since 1.0
     */
    MemberOrderResponseDto getMemberOrderData(String loginId);
}
