package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.exception.PublisherNotFoundException;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

import java.util.Objects;

/**
 * 출판사 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QueryPublisherServiceImpl implements QueryPublisherService {

    private final QueryPublisherRepository queryPublisherRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PublisherResponseDto findById(Long id) {
        Publisher publisher = queryPublisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));

        return new PublisherResponseDto(publisher.getId(), publisher.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PublisherResponseDto findByName(String name) {
        Publisher publisher = queryPublisherRepository.findByName(name).orElse(null);

        if (Objects.isNull(publisher)) {
            return null;
        }
        return new PublisherResponseDto(publisher.getId(), publisher.getName());
    }
}
