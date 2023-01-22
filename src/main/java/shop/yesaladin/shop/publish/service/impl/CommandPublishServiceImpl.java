package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.repository.CommandPublishRepository;
import shop.yesaladin.shop.publish.domain.repository.QueryPublishRepository;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.exception.PublishNotFoundException;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;

/**
 * 출판 관계 등록을 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandPublishServiceImpl implements CommandPublishService {

    private final CommandPublishRepository commandPublishRepository;
    private final QueryPublishRepository queryPublishRepository;

    /**
     * 출판을 DB에 등록하고 등록된 출판 Dto를 반환합니다.
     *
     * @param publish 출판관계 엔터티
     * @return 등록된 출판 Dto
     * @author 이수정
     * @since 1.0
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
     * 출판 관계를 삭제합니다.
     *
     * @param product 삭제하고자 하는 출판의 상품
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public void deleteByProduct(Product product) {
        if (!queryPublishRepository.existsByProduct(product)) {
            throw new PublishNotFoundException(product);
        }
        commandPublishRepository.deleteByProduct(product);
    }
}
