package shop.yesaladin.shop.publish.dto;

import lombok.*;
import shop.yesaladin.shop.product.domain.model.Product;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PublishResponseDto {

    private Pk pk;
    private LocalDate publishedDate;
    private Product product;
    private Publisher publisher;
}
