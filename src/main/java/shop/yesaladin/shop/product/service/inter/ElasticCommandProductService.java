package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.dto.SearchedProductUpdateDto;

/**
 * 엘라스틱서치에 데이터를 수정, 삭제하는 서비스 인터페이스
 *
 * @author 김선홍
 * @since 1.0
 */
public interface ElasticCommandProductService {

    /**
     * 엘라스틱서치 상품 업데이트 메서드
     *
     * @param id 수정할 상품의 id
     * @param updateDto 수정된 상품 정보
     * @return 수정된 상품
     * @author 김선홍
     * @since 1.0
     */
    SearchedProduct update(Long id,SearchedProductUpdateDto updateDto);

    /**
     * 엘라스틱서치에 상품을 삭제하는 메서드
     *
     * @param id 삭제할 상품의 id
     * @author 김선홍
     * @since 1.0
     */
    void delete(Long id);
}
