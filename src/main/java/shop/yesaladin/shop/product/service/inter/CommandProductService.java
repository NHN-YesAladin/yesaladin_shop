package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.ProductUpdateDto;

/**
 * 상품 등록/수정/삭제을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandProductService {

    /**
     * 상품을 생성하여 저장합니다. 생성된 상품 객체를 리턴합니다.
     *
     * @param dto 관리자에게서 입력받은 상품 생성정보
     * @return 생성된 상품 객체
     * @author 이수정
     * @since 1.0
     */
    ProductOnlyIdDto create(ProductCreateDto dto);

    /**
     * 상품 정보를 수정합니다. 수정된 상품 객체를 리턴합니다.
     *
     * @param id  수정할 상품의 Id
     * @param dto 상품 정보 수정을 위한 Dto
     * @return 수정된 상품 정보를 담은 Dto
     * @author 이수정
     * @since 1.0
     */
    ProductOnlyIdDto update(Long id, ProductUpdateDto dto);

    /**
     * 상품을 삭제상태로 변경합니다.
     *
     * @param id 삭제할 상품의 Id
     * @author 이수정
     * @since 1.0
     */
    void softDelete(Long id);

    /**
     * 상품 재고수량을 지정한 수량만큼 차감합니다.
     *
     * @param id       차감할 상품의 Id
     * @param quantity 차감할 수량
     * @author 이수정
     * @since 1.0
     */
    void deductQuantity(Long id, int quantity);

    /**
     * 상품의 판매여부를 변경합니다.
     *
     * @param id 판매여부를 변경할 상품의 Id
     * @author 이수정
     * @since 1.0
     */
    void changeIsSale(long id);

    /**
     * 상품의 강제품절여부를 변경합니다.
     *
     * @param id 강제품절여부를 변경할 상품의 Id
     * @author 이수정
     * @since 1.0
     */
    void changeIsForcedOutOfStock(long id);
}
