package shop.yesaladin.shop.publish.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.dto.PublishersResponseDto;

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
     * 출판사를 전체 조회하여 전체 조회한 Dto List를 반환합니다..
     *
     * @return 출판사 전체 조회한 List
     * @author 이수정
     * @since 1.0
     */
    List<PublisherResponseDto> findAll();

    /**
     * 페이징된 관리자용 출판사 리스트를 반환합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 조회된 출판사 dto를 담은 객체
     * @author 이수정
     * @since 1.0
     */
    Page<PublishersResponseDto> findAllForManager(Pageable pageable);
}
