package shop.yesaladin.shop.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.CommandPublisherRepository;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherRequestDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.exception.PublisherAlreadyExistsException;
import shop.yesaladin.shop.publish.exception.PublisherNotFoundException;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;

/**
 * 출판사 등록을 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandPublisherServiceImpl implements CommandPublisherService {

    private final CommandPublisherRepository commandPublisherRepository;
    private final QueryPublisherRepository queryPublisherRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public PublisherResponseDto register(Publisher publisher) {
        if (queryPublisherRepository.existsByName(publisher.getName())) {
            throw new PublisherAlreadyExistsException(publisher.getName());
        }
        commandPublisherRepository.save(publisher);

        return new PublisherResponseDto(publisher.getId(), publisher.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public PublisherResponseDto create(PublisherRequestDto createDto) {
        if (queryPublisherRepository.existsByName(createDto.getName())) {
            throw new PublisherAlreadyExistsException(createDto.getName());
        }

        Publisher publisher = Publisher.builder().name(createDto.getName()).build();

        commandPublisherRepository.save(publisher);

        return new PublisherResponseDto(
                publisher.getId(),
                publisher.getName()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public PublisherResponseDto modify(Long id, PublisherRequestDto modifyDto) {
        Publisher publisher = queryPublisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));

        String modifyName = modifyDto.getName();
        if (!publisher.getName().equals(modifyName)) {
            if (queryPublisherRepository.existsByName(modifyName)) {
                throw new PublisherAlreadyExistsException(modifyName);
            }
            publisher.changeName(modifyName);
        }

        commandPublisherRepository.save(publisher);

        return new PublisherResponseDto(
                publisher.getId(),
                publisher.getName()
        );
    }
}

