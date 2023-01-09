package shop.yesaladin.shop.tag.service.inter;

import shop.yesaladin.shop.tag.domain.model.ProductTag;

/**
 * 태그 관계 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandProductTagService {

    ProductTag register(ProductTag productTag);
}
