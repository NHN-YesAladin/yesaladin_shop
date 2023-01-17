package shop.yesaladin.shop.point.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.repository.CommandPointHistoryRepository;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;

/**
 * 포인트 내역 등록을 위한 service 구현체입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandPointHistoryServiceImpl implements CommandPointHistoryService {

    private final CommandPointHistoryRepository commandPointHistoryRepository;
    private final QueryMemberService queryMemberService;

    @Override
    @Transactional
    public PointHistoryResponseDto use(long memberId, PointHistoryRequestDto request) {
        PointHistory pointHistory = createPointHistory(
                memberId,
                request,
                PointCode.USE
        );
        return PointHistoryResponseDto.fromEntity(commandPointHistoryRepository.save(pointHistory));
    }

    @Override
    @Transactional
    public PointHistoryResponseDto save(long memberId, PointHistoryRequestDto request) {
        PointHistory pointHistory = createPointHistory(
                memberId,
                request,
                PointCode.SAVE
        );
        return PointHistoryResponseDto.fromEntity(commandPointHistoryRepository.save(pointHistory));
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
