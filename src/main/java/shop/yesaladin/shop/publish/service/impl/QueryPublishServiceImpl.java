package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.repository.QueryPublishRepository;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;

/**
 * 출판 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryPublishServiceImpl implements QueryPublishService {

    private final QueryPublishRepository queryPublishRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PublishResponseDto findByProduct(Product product) {
        Publish publish = queryPublishRepository.findByProduct(product)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PUBLISH_NOT_FOUND,
                        "Publish not found with id : " + product.getId()));

        return new PublishResponseDto(
                publish.getPk(),
                publish.getPublishedDate(),
                publish.getProduct(),
                publish.getPublisher()
        );
    }
}
