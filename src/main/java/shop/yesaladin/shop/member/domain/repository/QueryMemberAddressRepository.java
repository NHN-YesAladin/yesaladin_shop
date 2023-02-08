package shop.yesaladin.shop.member.domain.repository;

import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberAddressOrderSheetResponseDto;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;

/**
 * 회원 배송지 조회 관련 repository 인터페이스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryMemberAddressRepository {

    /**
     * 회원의 등록된 배송지 개수를 반환합니다.
     *
     * @param loginId 회원의 아이디
     * @return 회원의 배송지 개수
     * @author 최예린
     * @since 1.0
     */
    long countByLoginId(String loginId);

    /**
     * 배송지 pk를 통해 배송지를 조회합니다.
     *
     * @param id pk
     * @return 배송지
     * @author 최예린
     * @since 1.0
     */
    Optional<MemberAddress> findById(long id);

    /**
     * 회원의 특정한 배송지를 조회합니다.
     *
     * @param loginId         회원 아이디
     * @param memberAddressId 배송지 id
     * @return 회원의 배송지
     * @author 최예린
     * @since 1.0
     */
    Optional<MemberAddress> findByLoginIdAndMemberAddressId(
            String loginId,
            long memberAddressId
    );

    /**
     * 배송지 id를 통해 배송지를 조회합니다.
     *
     * @param id pk
     * @return 배송지 데이터
     * @author 최예린
     * @since 1.0
     */
    Optional<MemberAddressResponseDto> getById(long id);

    /**
     * 회원 id를 통해 회원의 배송지를 조회합니다.
     *
     * @param loginId 회원 아이디
     * @return 회원의 배송지 데이터 목록
     * @author 최예린
     * @since 1.0
     */
    List<MemberAddressResponseDto> getByLoginId(String loginId);

    /**
     * 회원의 특정한 배송지를 조회합니다.
     *
     * @param loginId         회원 아이디
     * @param memberAddressId 배송지 id
     * @return 회원의 배송지
     * @author 최예린
     * @since 1.0
     */
    Optional<MemberAddressResponseDto> getByLoginIdAndMemberAddressId(
            String loginId,
            long memberAddressId
    );

    /**
     * 회원의 특정한 배송지가 있는지 확인합니다.
     *
     * @param loginId         회원 id
     * @param memberAddressId 배송지 id
     * @return 회원의 배송지 유무
     * @author 최예린
     * @since 1.0
     */
    boolean existByLoginIdAndMemberAddressId(String loginId, long memberAddressId);
}
