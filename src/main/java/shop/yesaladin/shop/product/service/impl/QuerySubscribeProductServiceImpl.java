package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.repository.QuerySubscribeProductRepository;
import shop.yesaladin.shop.product.service.inter.QuerySubscribeProductService;

/**
 * 구독상품 조회를 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QuerySubscribeProductServiceImpl implements QuerySubscribeProductService {

    private final QuerySubscribeProductRepository querySubscribeProductRepository;

    /**
     * ISSN으로 이미 저장되어있는 ISSN인지 확인하고, 존재한다면 구독상품 엔터티를 반환, 존재하지 않는다면 null을 반환합니다.
     *
     * @param ISSN 찾고자 하는 구독상품의 ISSN
     * @return 찾은 구독상품 엔터티 or null
     * @author 이수정
     * @since 1.0
     */
    @Override
    public SubscribeProduct findByISSN(String ISSN) {
        return querySubscribeProductRepository.findByISSN(ISSN).orElse(null);
    }
}
