package shop.yesaladin.shop.tag.service.inter;

import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;


/**
 * 태그 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryTagService {

    /**
     * Id에 해당하는 태그를 조회하여 Dto로 반환합니다.
     *
     * @param id 태그를 찾아낼 id
     * @return 조회된 태그 dto
     * @author 이수정
     * @since 1.0
     */
    TagResponseDto findById(Long id);

    /**
     * 페이징된 관리자용 태그 리스트를 반환합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 조회된 태그 dto를 담은 객체
     * @author 이수정
     * @since 1.0
     */
    PaginatedResponseDto<TagResponseDto> findAllForManager(Pageable pageable);
}
