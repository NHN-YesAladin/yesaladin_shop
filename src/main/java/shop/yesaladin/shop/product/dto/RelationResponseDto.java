package shop.yesaladin.shop.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 연관관계 데이터를 반환받는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RelationResponseDto {

    private long id;

    private String thumbnailFileUrl;
    private String title;
    private List<String> authors;
    private String publisher;

    private long sellingPrice;
    private int discountRate;

    private Boolean isShown;
}
