package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.exception.PublisherNotFoundException;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * 출판사를 전체 조회하여 전체 조회한 Dto List를 반환합니다..
     *
     * @return 출판사 전체 조회한 List
     * @author 이수정
     * @since 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public List<PublisherResponseDto> findAll() {
        return queryPublisherRepository.findAll().stream()
                .map(publisher -> new PublisherResponseDto(publisher.getId(), publisher.getName()))
                .collect(Collectors.toList());
    }
}
