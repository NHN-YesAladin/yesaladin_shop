package shop.yesaladin.shop.member.service.inter;

import shop.yesaladin.shop.member.dto.MemberAddressCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberAddressCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberAddressUpdateResponseDto;

/**
 * 회원지 배송을 등록/수정/삭제를 위한 service interface
 *
 * @author 최예린
 * @since 1.0
 */
public interface CommandMemberAddressService {

    /**
     * 회원의 배송지를 등록합니다.
     *
     * @param loginId 회원의 아이디
     * @param request  등록할 배송지 데이터
     * @return 등록된 배송지 데이터
     * @author 최예린
     * @since 1.0
     */
    MemberAddressCreateResponseDto save(String loginId, MemberAddressCreateRequestDto request);

    /**
     * 회원의 대표 배송지를 등록합니다.
     *
     * @param loginId  회원의 아이디
     * @param addressId 대표 배송지 아이디
     * @return 대표 배송지 데이터
     * @author 최예린
     * @since 1.0
     */
    MemberAddressUpdateResponseDto markAsDefault(String loginId, long addressId);

    /**
     * 회원의 배송지를 삭제합니다.
     *
     * @param loginId  회원의 아이디
     * @param addressId 삭제할 배송지 id
     * @return 삭제된 회원의 배송지 id
     * @author 최예린
     * @since 1.0
     */
    long delete(String loginId, long addressId);
}
