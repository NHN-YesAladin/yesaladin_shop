package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandTotalDiscountRateRepository;
import shop.yesaladin.shop.product.service.inter.CommandTotalDiscountRateService;

/**
 * 전체 할인율 등록을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandTotalDiscountRateServiceImpl implements CommandTotalDiscountRateService {

    private final CommandTotalDiscountRateRepository commandTotalDiscountRateRepository;

    /**
     * 전체 할인율을 DB에 등록하고, 등록한 전체 할인율 객체를 리턴합니다.
     *
     * @param totalDiscountRate 전체 할인율 엔터티
     * @return 등록된 전체 할인율 객체
     * @author 이수정
     * @since 1.0
     */
    @Override
    public TotalDiscountRate register(TotalDiscountRate totalDiscountRate) {
        return commandTotalDiscountRateRepository.save(totalDiscountRate);
    }
}
