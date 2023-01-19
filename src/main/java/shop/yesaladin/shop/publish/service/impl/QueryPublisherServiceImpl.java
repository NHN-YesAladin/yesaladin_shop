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
     * ID에 해당하는 출판사를 조회하여 Dto로 반환합니다.
     *
     * @param id 출판사를 찾아낼 id
     * @return 조회된 출판사 dto
     * @author 이수정
     * @since 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public PublisherResponseDto findById(Long id) {
        Publisher publisher = queryPublisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));

        return new PublisherResponseDto(publisher.getId(), publisher.getName());
    }

    /**
     * 출판사 이름으로 이미 저장되어있는 출판사인지 확인하고, 존재한다면 출판사 Dto를 반환, 존재하지 않는다면 null을 반환합니다.
     *
     * @param name 찾고자하는 출판사 이름
     * @return 찾은 출판사 Dto or null
     * @author 이수정
     * @since 1.0
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
