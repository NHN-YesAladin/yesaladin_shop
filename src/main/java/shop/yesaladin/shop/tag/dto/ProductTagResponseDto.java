package shop.yesaladin.shop.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.tag.domain.model.ProductTag.Pk;
import shop.yesaladin.shop.tag.domain.model.Tag;

/**
 * 태그관계 데이터를 반환받는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagResponseDto {

    private Pk pk;
    private Product product;
    private Tag tag;
}
