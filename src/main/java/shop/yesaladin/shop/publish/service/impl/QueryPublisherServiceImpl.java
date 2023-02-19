package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

import java.util.ArrayList;
import java.util.List;

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
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PUBLISH_PUBLISHER_NOT_FOUND,
                        "Publisher not found with id : " + id));

        return PublisherResponseDto.builder().id(publisher.getId()).name(publisher.getName()).build();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<PublisherResponseDto> findAllForManager(Pageable pageable) {
        return getPublisherResponseDto(queryPublisherRepository.findAllForManager(pageable));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<PublisherResponseDto> findByNameForManager(
            String name,
            Pageable pageable
    ) {
        return getPublisherResponseDto(queryPublisherRepository.findByNameForManager(name, pageable));
    }

    private PaginatedResponseDto<PublisherResponseDto> getPublisherResponseDto(Page<Publisher> page) {
        List<PublisherResponseDto> publishers = new ArrayList<>();
        for (Publisher publisher : page.getContent()) {
            publishers.add(PublisherResponseDto.builder().id(publisher.getId()).name(publisher.getName()).build());
        }

        return PaginatedResponseDto.<PublisherResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(publishers)
                .build();
    }
}
