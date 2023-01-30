package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.dto.PublishersResponseDto;
import shop.yesaladin.shop.publish.exception.PublisherNotFoundException;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 출판사 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
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
    public List<PublisherResponseDto> findAll() {
        return queryPublisherRepository.findAll().stream()
                .map(publisher -> new PublisherResponseDto(publisher.getId(), publisher.getName()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<PublishersResponseDto> findAllForManager(Pageable pageable) {
        Page<Publisher> page = queryPublisherRepository.findAllForManager(pageable);

        List<PublishersResponseDto> publishers = new ArrayList<>();
        for (Publisher publisher : page.getContent()) {
            publishers.add(new PublishersResponseDto(
                    publisher.getId(),
                    publisher.getName()
            ));
        }

        return new PageImpl<>(publishers, pageable, page.getTotalElements());
    }
}
