package shop.yesaladin.shop.member.domain.repository;

import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberAddress;

/**
 * 회원 배송지 조회 관련 repository 인터페이스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryMemberAddressRepository {

    /**
     * 회원 id를 통해 회원의 배송지를 조회합니다.
     *
     * @param memberId 회원의 아이디
     * @return 회원의 배송지 데이터 목록
     * @author 최예린
     * @since 1.0
     */
    List<MemberAddress> findByMemberId(Long memberId);

    /**
     * 배송지 id를 통해 배송지를 조회합니다.
     *
     * @param id pk
     * @return 배송지 데이터
     * @author 최예린
     * @since 1.0
     */
    Optional<MemberAddress> findById(Long id);
}
