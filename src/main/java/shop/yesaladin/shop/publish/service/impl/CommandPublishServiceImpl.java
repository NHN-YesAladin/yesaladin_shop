package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.repository.CommandPublishRepository;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;

/**
 * 출판 관계 등록을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandPublishServiceImpl implements CommandPublishService {

    private final CommandPublishRepository commandPublishRepository;

    /**
     * 출판을 DB에 등록합니다.
     *
     * @param publish 출판관계 엔터티
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Publish register(Publish publish) {
        return commandPublishRepository.save(publish);
    }
}
