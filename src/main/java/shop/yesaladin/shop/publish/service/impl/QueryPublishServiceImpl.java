package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.repository.QueryPublishRepository;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.exception.PublishNotFoundException;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;

/**
 * 출판 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QueryPublishServiceImpl implements QueryPublishService {

    private final QueryPublishRepository queryPublishRepository;

    /**
     * 출판한 상품을 기준으로 출판를 조회하여 조회된 출판 Dto를 반환합니다.
     *
     * @param product 출판을 조회할 product
     * @return 조회된 출판 dto
     * @author 이수정
     * @since 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public PublishResponseDto findByProduct(Product product) {
        Publish publish = queryPublishRepository.findByProduct(product)
                .orElseThrow(() -> new PublishNotFoundException(product));

        return new PublishResponseDto(
                publish.getPk(),
                publish.getPublishedDate(),
                publish.getProduct(),
                publish.getPublisher()
        );
    }
}
