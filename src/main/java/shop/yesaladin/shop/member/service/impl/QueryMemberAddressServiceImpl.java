package shop.yesaladin.shop.member.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;

/**
 * 회원배송지 조회 관련 service 구현체입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryMemberAddressServiceImpl implements QueryMemberAddressService {

    private final QueryMemberRepository queryMemberRepository;
    private final QueryMemberAddressRepository queryMemberAddressRepository;

    private final String MEMBER_ADDRESS_NOT_FOUND = "MemberAddress not found with id : ";

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MemberAddress findById(long id) {
        return queryMemberAddressRepository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ADDRESS_NOT_FOUND,
                        MEMBER_ADDRESS_NOT_FOUND + id
                ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MemberAddressResponseDto getById(long id) {
        return queryMemberAddressRepository.getById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ADDRESS_NOT_FOUND,
                        MEMBER_ADDRESS_NOT_FOUND + id
                ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MemberAddressResponseDto> getByLoginId(String loginId) {
        checkLoginIdIsExist(loginId);

        return queryMemberAddressRepository.getByLoginId(loginId);
    }

    private void checkLoginIdIsExist(String loginId) {
        if (!queryMemberRepository.existsMemberByLoginId(loginId)) {
            throw new ClientException(
                    ErrorCode.MEMBER_NOT_FOUND,
                    "Member not found with loginId : " + loginId
            );
        }
    }
}
