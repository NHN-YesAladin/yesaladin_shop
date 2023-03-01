package shop.yesaladin.shop.member.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRoleRepository;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberIdDto;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberOrderSheetResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberStatisticsResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원 조회용 서비스 구현체 입니다.
 *
 * @author 송학현
 * @author 최예린
 * @author 서민지
 * @author 김선홍
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QueryMemberServiceImpl implements QueryMemberService {

    private final QueryMemberRepository queryMemberRepository;
    private final QueryMemberRoleRepository queryMemberRoleRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberById(long id) {
        Member member = queryMemberRepository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member Id: " + id
                ));
        return MemberDto.fromEntity(member);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Member findByLoginId(String loginId) {
        return queryMemberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member not found with loginId : " + loginId
                ));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberByLoginId(String loginId) {
        Member member = getMemberByLoginId(
                loginId,
                queryMemberRepository.findMemberByLoginId(loginId),
                "Member Login Id: "
        );
        return MemberDto.fromEntity(member);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberDto findMemberByNickname(String nickname) {
        Member member = queryMemberRepository.findMemberByNickname(nickname)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member Nickname: " + nickname
                ));
        return MemberDto.fromEntity(member);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberLoginResponseDto findMemberLoginInfoByLoginId(String loginId) {
        Member member = getMemberByLoginId(
                loginId,
                queryMemberRepository.findMemberByLoginId(loginId),
                "Member Login Id: "
        );

        List<String> roles = queryMemberRoleRepository.findMemberRolesByMemberId(member.getId());

        return new MemberLoginResponseDto(
                member.getId(),
                member.getName(),
                member.getNickname(),
                member.getLoginId(),
                member.getEmail(),
                member.getPassword(),
                roles
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberManagerResponseDto> findMemberManages(Pageable pageable) {
        return queryMemberRepository.findMemberManagers(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MemberManagerResponseDto> findMemberManagesByLoginId(
            String loginId,
            Pageable pageable
    ) {
        return queryMemberRepository.findMemberManagersByLoginId(loginId, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MemberManagerResponseDto> findMemberManagesByNickName(
            String nickname,
            Pageable pageable
    ) {
        return queryMemberRepository.findMemberManagersByNickname(nickname, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MemberManagerResponseDto> findMemberManagesByPhone(
            String phone,
            Pageable pageable
    ) {
        return queryMemberRepository.findMemberManagersByPhone(phone, pageable);
    }


    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MemberManagerResponseDto> findMemberManagesByName(String name, Pageable pageable) {
        return queryMemberRepository.findMemberManagersByName(name, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MemberManagerResponseDto> findMemberManagesBySignUpDate(
            LocalDate signUpDate,
            Pageable pageable
    ) {
        return queryMemberRepository.findMemberManagersBySignUpDate(signUpDate, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<MemberIdDto> findMemberIdsByBirthday(int laterDays) {
        LocalDate birthday = LocalDate.now().plusDays(laterDays);
        return queryMemberRepository.findMemberIdsByBirthday(
                birthday.getMonthValue(),
                birthday.getDayOfMonth()
        );
    }

    private Member getMemberByLoginId(String loginId, Optional<Member> memberByLoginId, String s) {
        return memberByLoginId.orElseThrow(() -> new ClientException(
                ErrorCode.MEMBER_NOT_FOUND,
                s + loginId
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsLoginId(String loginId) {
        return queryMemberRepository.existsMemberByLoginId(loginId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsNickname(String nickname) {
        return queryMemberRepository.existsMemberByNickname(nickname);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsEmail(String email) {
        return queryMemberRepository.existsMemberByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsPhone(String phone) {
        return queryMemberRepository.existsMemberByPhone(phone);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberGradeQueryResponseDto getMemberGradeByLoginId(String loginId) {
        return MemberGradeQueryResponseDto.fromEntity(queryMemberRepository.findMemberByLoginId(
                        loginId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member not found with loginId : " + loginId
                )));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public MemberQueryResponseDto getByLoginId(String loginId) {
        return MemberQueryResponseDto.fromEntity(queryMemberRepository.findMemberByLoginId(
                        loginId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member not found with loginId : " + loginId
                )));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MemberOrderSheetResponseDto getMemberForOrder(String loginId) {
        return queryMemberRepository.getMemberOrderData(loginId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.COUPON_NOT_FOUND,
                        "Member Coupon not found with loginId : " + loginId
                ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MemberStatisticsResponseDto getMemberStatistics() {
        return MemberStatisticsResponseDto.builder()
                .totalMembers(queryMemberRepository.countTotalMembers())
                .totalBlockedMembers(queryMemberRepository.countBlockedMembers())
                .totalWithdrawMembers(queryMemberRepository.countWithdrawMembers())
                .totalWhiteGrades(queryMemberRepository.countWhiteMembers())
                .totalBronzeGrades(queryMemberRepository.countBronzeMembers())
                .totalSilverGrades(queryMemberRepository.countSilverMembers())
                .totalGoldGrades(queryMemberRepository.countGoldMembers())
                .totalPlatinumGrades(queryMemberRepository.countPlatinumMembers())
                .build();
    }
}
