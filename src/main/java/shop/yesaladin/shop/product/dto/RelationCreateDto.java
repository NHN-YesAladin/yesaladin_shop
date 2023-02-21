package shop.yesaladin.shop.product.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 연관관계를 생성하기 위한 정보를 받아오는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RelationCreateDto {

    @NotNull
    @Positive
    Long productSubId;
}
