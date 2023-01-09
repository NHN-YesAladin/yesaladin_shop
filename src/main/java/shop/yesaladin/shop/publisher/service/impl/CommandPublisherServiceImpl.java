package shop.yesaladin.shop.publisher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.publisher.domain.model.Publisher;
import shop.yesaladin.shop.publisher.domain.repository.CommandPublisherRepository;
import shop.yesaladin.shop.publisher.service.inter.CommandPublisherService;

/**
 * 출판사 등록을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandPublisherServiceImpl implements CommandPublisherService {

    private final CommandPublisherRepository commandPublisherRepository;

    /**
     * 출판사를 DB에 등록하고, 저장한 출판사 객체를 리턴합니다.
     *
     * @param publisher 출판사 엔터티
     * @return 저장된 출판사 객체
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Publisher register(Publisher publisher) {
        return commandPublisherRepository.save(publisher);
    }
}

