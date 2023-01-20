package shop.yesaladin.shop.writing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.model.Writing;

/**
 * 집필 데이터를 반환받는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WritingResponseDto {

    private Product product;
    private Author author;

    /**
     * Dto를 바탕으로 집필 엔터티를 생성해 반환합니다.
     *
     * @return 생성된 집필 엔터티
     * @author 이수정
     * @since 1.0
     */
    public Writing toEntity() {
        return Writing.create(product, author);
    }
}
