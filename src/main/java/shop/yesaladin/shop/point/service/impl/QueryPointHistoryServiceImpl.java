package shop.yesaladin.shop.point.service.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.dto.PointResponseDto;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;

/**
 * {@inheritDoc}
 */
@RequiredArgsConstructor
@Service
public class QueryPointHistoryServiceImpl implements QueryPointHistoryService {

    private final QueryPointHistoryRepository queryPointHistoryRepository;
    private final QueryMemberService queryMemberService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PointHistoryResponseDto> getPointHistoriesWithLoginIdAndCode(
            String loginId,
            PointCode pointCode,
            Pageable pageable
    ) {
        return queryPointHistoryRepository.getByLoginIdAndPointCode(loginId, pointCode, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PointHistoryResponseDto> getPointHistoriesWithLoginId(
            String loginId,
            Pageable pageable
    ) {
        return queryPointHistoryRepository.getByLoginId(loginId, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PointHistoryResponseDto> getPointHistoriesWithCode(
            PointCode pointCode,
            Pageable pageable
    ) {
        return queryPointHistoryRepository.getByPointCode(pointCode, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PointHistoryResponseDto> getPointHistories(Pageable pageable) {
        return queryPointHistoryRepository.getBy(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PointResponseDto getMemberPoint(String loginId) {
        if(Objects.isNull(queryMemberService.findMemberByLoginId(loginId))) {
            throw new MemberNotFoundException("Member loginId : " + loginId);
        }
        return new PointResponseDto(queryPointHistoryRepository.getMemberPointByLoginId(loginId));
    }
}
