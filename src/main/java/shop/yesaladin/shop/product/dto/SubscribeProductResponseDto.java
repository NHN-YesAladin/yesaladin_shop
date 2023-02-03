package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

/**
 * 구독상품 데이터를 반환받는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeProductResponseDto {

    private long id;
    private String issn;

    public SubscribeProduct toEntity() {
        return SubscribeProduct.builder().id(id).ISSN(issn).build();
    }
}
