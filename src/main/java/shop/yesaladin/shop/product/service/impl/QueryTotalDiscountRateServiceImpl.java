package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.QueryTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.TotalDiscountRateResponseDto;
import shop.yesaladin.shop.product.exception.TotalDiscountRateNotExistsException;
import shop.yesaladin.shop.product.service.inter.QueryTotalDiscountRateService;


@Service
@RequiredArgsConstructor
public class QueryTotalDiscountRateServiceImpl implements QueryTotalDiscountRateService {

    private final QueryTotalDiscountRateRepository queryTotalDiscountRateRepository;

    /**
     * 전체 할인율이 존재하는지 확인하여 반환합니다.
     *
     * @param id 전체 할인율이 저장되어있는 row의 id(default 1)
     * @return 찾은 전체 할인율 엔터티 or null
     * @throws TotalDiscountRateNotExistsException 전체할인율이 존재하지 않는다면 예외를 던집니다.
     * @author 이수정
     * @since 1.0
     */
    @Override
    public TotalDiscountRateResponseDto findById(int id) {
        TotalDiscountRate totalDiscountRate = queryTotalDiscountRateRepository.findById(id)
                .orElseThrow(() -> new TotalDiscountRateNotExistsException());

        return new TotalDiscountRateResponseDto(
                totalDiscountRate.getId(),
                totalDiscountRate.getDiscountRate()
        );
    }
}
