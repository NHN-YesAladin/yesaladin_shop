package shop.yesaladin.shop.publish.service.inter;

import shop.yesaladin.shop.publish.dto.PublisherResponseDto;

import java.util.List;

/**
 * 출판사 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryPublisherService {

    /**
     * ID에 해당하는 출판사를 조회하여 Dto로 반환합니다.
     *
     * @param id 출판사를 찾아낼 id
     * @return 조회된 출판사 dto
     * @author 이수정
     * @since 1.0
     */
    PublisherResponseDto findById(Long id);

    /**
     * 출판사 이름으로 이미 저장되어있는 출판사인지 확인하고, 존재한다면 출판사 Dto를 반환, 존재하지 않는다면 null을 반환합니다.
     *
     * @param name 찾고자하는 출판사 이름
     * @return 찾은 출판사 Dto or null
     * @author 이수정
     * @since 1.0
     */
    PublisherResponseDto findByName(String name);
}
