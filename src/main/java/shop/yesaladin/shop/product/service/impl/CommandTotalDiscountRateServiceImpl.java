package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.TotalDiscountRateResponseDto;
import shop.yesaladin.shop.product.service.inter.CommandTotalDiscountRateService;

/**
 * 전체 할인율 수정을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandTotalDiscountRateServiceImpl implements CommandTotalDiscountRateService {

    private final CommandTotalDiscountRateRepository commandTotalDiscountRateRepository;

    /**
     * 전체 할인율을 수정하여 DB에 저장하고, 수정한 전체 할인율 객체를 리턴합니다.
     *
     * @param totalDiscountRate 전체 할인율 엔터티
     * @return 등록된 전체 할인율 객체
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public TotalDiscountRateResponseDto modify(TotalDiscountRate totalDiscountRate) {
        TotalDiscountRate savedTotalDiscountRate = commandTotalDiscountRateRepository
                .save(totalDiscountRate);

        return new TotalDiscountRateResponseDto(
                savedTotalDiscountRate.getId(),
                savedTotalDiscountRate.getDiscountRate()
        );
    }
}
