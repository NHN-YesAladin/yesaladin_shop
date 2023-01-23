package shop.yesaladin.shop.publish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publish.Pk;
import shop.yesaladin.shop.publish.domain.model.Publisher;

import java.time.LocalDate;

/**
 * 출판 데이터를 반환받는 DTO 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishResponseDto {

    private Pk pk;
    private LocalDate publishedDate;
    private Product product;
    private Publisher publisher;

    /**
     * Dto를 바탕으로 출판 엔터티를 생성해 반환합니다.
     *
     * @return 출판 엔터티
     * @author 이수정
     * @since 1.0
     */
    public Publish toEntity() {
        return Publish.create(product, publisher, publishedDate.toString());
    }
}
