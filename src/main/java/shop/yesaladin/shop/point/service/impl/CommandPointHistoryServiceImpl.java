package shop.yesaladin.shop.point.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.repository.CommandPointHistoryRepository;
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
    public PointHistoryResponseDto use(long memberId, long amount) {
        PointHistory pointHistory = createPointHistory(
                memberId,
                amount,
                PointCode.USE
        );
        return PointHistoryResponseDto.fromEntity(commandPointHistoryRepository.save(pointHistory));
    }

    @Override
    public PointHistoryResponseDto save(long memberId, long amount) {
        PointHistory pointHistory = createPointHistory(
                memberId,
                amount,
                PointCode.SAVE
        );
        return PointHistoryResponseDto.fromEntity(commandPointHistoryRepository.save(pointHistory));
    }

    private PointHistory createPointHistory(long memberId, long amount, PointCode pointCode) {
        Member member = queryMemberService.findMemberById(memberId).toEntity();

        PointHistory pointHistory = PointHistory.builder()
                .amount(amount)
                .createDateTime(LocalDateTime.now())
                .pointCode(pointCode)
                .member(member).build();
        return pointHistory;
    }
}
