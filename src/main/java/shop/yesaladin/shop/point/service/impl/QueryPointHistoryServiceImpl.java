package shop.yesaladin.shop.point.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;

/**
 * {@inheritDoc}
 */
@RequiredArgsConstructor
@Service
public class QueryPointHistoryServiceImpl implements QueryPointHistoryService {

    private final QueryPointHistoryRepository queryPointHistoryRepository;

    @Override
    public Page<PointHistoryResponseDto> getPointHistoriesWithLoginIdAndCode(
            String loginId,
            PointCode pointCode,
            Pageable pageable
    ) {
        return queryPointHistoryRepository.getByLoginIdAndPointCode(loginId, pointCode, pageable);
    }

    @Override
    public Page<PointHistoryResponseDto> getPointHistoriesWithLoginId(
            String loginId,
            Pageable pageable
    ) {
        return queryPointHistoryRepository.getByLoginId(loginId, pageable);
    }

    @Override
    public Page<PointHistoryResponseDto> getPointHistoriesWithCode(
            PointCode pointCode,
            Pageable pageable
    ) {
        return queryPointHistoryRepository.getByPointCode(pointCode, pageable);
    }

    @Override
    public Page<PointHistoryResponseDto> getPointHistories(Pageable pageable) {
        return queryPointHistoryRepository.getBy(pageable);
    }
}
