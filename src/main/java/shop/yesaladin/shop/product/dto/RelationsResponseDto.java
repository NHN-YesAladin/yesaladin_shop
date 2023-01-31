package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 연관관계 전체 조회를 하여 Dto로 반환합니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RelationsResponseDto {

    private Long id;

    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;

    private long sellingPrice;
    private int rate;
}
