package shop.yesaladin.shop.product.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.product.dto.RelationsResponseDto;

/**
 * 상품 연관관계 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryRelationService {

    /**
     * 상품을 기준으로 상품 연관관계를 조회하여 조회된 상품 연관관계 Dto를 반환합니다.
     *
     * @param productId 연관관계를 조회할 product의 Id
     * @param pageable  페이징 처리를 위한 객체
     * @return 조회된 상품 연관관계 dto
     * @author 이수정
     * @since 1.0
     */
    Page<RelationsResponseDto> findAllForManager(Long productId, Pageable pageable);
}
