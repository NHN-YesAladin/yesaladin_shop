package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.repository.CommandPublishRepository;
import shop.yesaladin.shop.publish.domain.repository.QueryPublishRepository;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;

/**
 * 출판 관계 등록을 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandPublishServiceImpl implements CommandPublishService {

    private final CommandPublishRepository commandPublishRepository;
    private final QueryPublishRepository queryPublishRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public PublishResponseDto register(Publish publish) {
        Publish savedPublish = commandPublishRepository.save(publish);

        return new PublishResponseDto(
                savedPublish.getPk(),
                savedPublish.getPublishedDate(),
                savedPublish.getProduct(),
                savedPublish.getPublisher()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteByProduct(Product product) {
        if (!queryPublishRepository.existsByProduct(product)) {
            throw new ClientException(
                    ErrorCode.PUBLISH_NOT_FOUND,
                    "Publish not found with id : " + product.getId());
        }
        commandPublishRepository.deleteByProduct(product);
    }
}
