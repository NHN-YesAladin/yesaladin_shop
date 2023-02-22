package shop.yesaladin.shop.writing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.domain.model.Author;

/**
 * 집필 데이터를 반환받는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WritingResponseDto {

    private Product product;
    private Author author;
}
