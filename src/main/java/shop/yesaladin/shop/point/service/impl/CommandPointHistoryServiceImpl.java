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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public PointHistoryResponseDto use(PointHistoryRequestDto request) {
        PointHistory pointHistory = createPointHistory(request, PointCode.USE);
        checkMemberHasEnoughPoint(request);
        PointHistory savedPointHistory = commandPointHistoryRepository.save(pointHistory);

        return PointHistoryResponseDto.fromEntity(savedPointHistory);
    }

    private void checkMemberHasEnoughPoint(PointHistoryRequestDto request) {
        long amount = queryPointHistoryRepository.getMemberPointByLoginId(request.getLoginId());

        if (request.getAmount() > amount) {
            throw new OverPointUseException(request.getLoginId(), amount);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public PointHistoryResponseDto save(PointHistoryRequestDto request) {
        PointHistory pointHistory = createPointHistory(request, PointCode.SAVE);
        PointHistory savedPointHistory = commandPointHistoryRepository.save(pointHistory);

        return PointHistoryResponseDto.fromEntity(savedPointHistory);
    }

    private PointHistory createPointHistory(
            PointHistoryRequestDto request,
            PointCode pointCode
    ) {
        Member member = queryMemberService.findMemberByLoginId(request.getLoginId()).toEntity();

        return request.toEntity(pointCode, member);
    }
}
