package shop.yesaladin.shop.writing.service.inter;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;

import java.util.List;

/**
 * 집필 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryWritingService {

    /**
     * 해당 상품의 집필 관계를 조회하여 Dto List로 반환합니다.
     *
     * @param product 관계를 조회할 상품
     * @return 조회된 집필 관계 dto List
     * @author 이수정
     * @since 1.0
     */
    List<WritingResponseDto> findByProduct(Product product);
}
