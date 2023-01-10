package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.repository.CommandSubscribeProductRepository;
import shop.yesaladin.shop.product.dto.SubscribeProductResponseDto;
import shop.yesaladin.shop.product.service.inter.CommandSubscribeProductService;

/**
 * 구독상품 등록을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandSubscribeProductServiceImpl implements CommandSubscribeProductService {

    private final CommandSubscribeProductRepository commandSubscribeProductRepository;

    /**
     * 구독상품를 DB에 등록하고, 등록한 구독상품 객체를 리턴합니다.
     *
     * @param subscribeProduct 구독상품 엔터티
     * @return 등록된 구독상품 객체
     * @author 이수정
     * @since 1.0
     */
    @Override
    public SubscribeProductResponseDto register(SubscribeProduct subscribeProduct) {
        SubscribeProduct savedSubscribeProduct = commandSubscribeProductRepository.save(subscribeProduct);

        return new SubscribeProductResponseDto(savedSubscribeProduct.getId(), savedSubscribeProduct.getISSN());
    }
}
