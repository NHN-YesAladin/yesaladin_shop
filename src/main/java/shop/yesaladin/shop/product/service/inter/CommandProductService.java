package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductSavingMethodCode;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.publisher.domain.model.Publisher;

/**자
 * 상품 생성을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandProductService {

    Product create(ProductCreateDto dto, Publisher publisher, SubscribeProduct subscribeProduct, File file, ProductTypeCode productTypeCode, ProductSavingMethodCode productSavingMethodCode);
}
