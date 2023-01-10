package shop.yesaladin.shop.member.domain.repository;

import shop.yesaladin.shop.member.domain.model.MemberAddress;

/**
 * 회원 배송지 등록/수정/삭제 관련 repository 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandMemberAddressRepository {

    /**
     * 회원 배송지 데이터를 등록합니다.
     *
     * @param memberAddress 등록할 회원 배송지 데이터
     * @return 등록된 회원 배송지 데이터
     * @author 최예린
     * @since 1.0
     */
    MemberAddress save(MemberAddress memberAddress);

    /**
     * 회원의 배송지 데이터를 삭제합니다.
     *
     * @param id 삭제할 회원 배송지 pk
     * @author 최예린
     * @since 1.0
     */
    void deleteById(Long id);

    /**
     * 회원의 배송지 목록에서 대표 배송지를 제거합니다.
     *
     * @param memberId 회원 id
     * @author 최예린
     * @since 1.0
     */
    void updateIsDefaultToFalseByMemberId(Long memberId);
}
