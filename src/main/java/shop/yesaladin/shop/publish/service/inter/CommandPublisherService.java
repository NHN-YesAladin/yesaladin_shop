package shop.yesaladin.shop.publish.service.inter;

import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.dto.PublisherRequestDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;

/**
 * 출판사 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandPublisherService {

    /**
     * 출판사를 DB에 등록하고, 저장한 출판사 Dto를 반환합니다.
     *
     * @param publisher 출판사 엔터티
     * @return 저장된 출판사 Dto
     * @author 이수정
     * @since 1.0
     */
    PublisherResponseDto register(Publisher publisher);

    /**
     * 출판사를 생성하여 저장하고, 생성된 출판사 dto를 반환합니다.
     *
     * @param createDto 생성할 출판사의 정보를 가진 Dto
     * @author 이수정
     * @since 1.0
     */
    PublisherResponseDto create(PublisherRequestDto createDto);

    /**
     * 출판사를 수정하고 수정된 출판사 dto를 반환합니다.
     *
     * @param modifyDto 수정된 출판사의 정보를 가진 Dto
     * @author 이수정
     * @since 1.0
     */
    PublisherResponseDto modify(Long id, PublisherRequestDto modifyDto);
}
