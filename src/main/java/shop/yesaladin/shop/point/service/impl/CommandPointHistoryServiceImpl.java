package shop.yesaladin.shop.point.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.repository.CommandPointHistoryRepository;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.exception.OverPointUseException;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;

/**
 * 포인트 내역 등록을 위한 service 구현체입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandPointHistoryServiceImpl implements CommandPointHistoryService {

    private final CommandPointHistoryRepository commandPointHistoryRepository;
    private final QueryPointHistoryRepository queryPointHistoryRepository;
    private final QueryMemberService queryMemberService;

    @Override
    @Transactional
    public PointHistoryResponseDto use(long memberId, PointHistoryRequestDto request) {
        PointHistory pointHistory = createPointHistory(
                memberId,
                request,
                PointCode.USE
        );
        checkMemberHasEnoughPoint(memberId, request);
        PointHistory savedPointHistory = commandPointHistoryRepository.save(pointHistory);

        return PointHistoryResponseDto.fromEntity(savedPointHistory);
    }

    private void checkMemberHasEnoughPoint(long memberId, PointHistoryRequestDto request) {
        long amount = queryPointHistoryRepository.getMemberPointByMemberId(memberId);

        if (request.getAmount() > amount) {
            throw new OverPointUseException(memberId, amount);
        }
    }

    @Override
    @Transactional
    public PointHistoryResponseDto save(long memberId, PointHistoryRequestDto request) {
        PointHistory pointHistory = createPointHistory(
                memberId,
                request,
                PointCode.SAVE
        );
        PointHistory savedPointHistory = commandPointHistoryRepository.save(pointHistory);

        return PointHistoryResponseDto.fromEntity(savedPointHistory);
    }

    private PointHistory createPointHistory(
            long memberId,
            PointHistoryRequestDto request,
            PointCode pointCode
    ) {
        Member member = queryMemberService.findMemberById(memberId).toEntity();

        return request.toEntity(pointCode, member);
    }
}
