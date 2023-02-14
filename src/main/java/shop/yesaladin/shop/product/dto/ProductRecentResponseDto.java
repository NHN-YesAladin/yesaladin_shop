package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.product.domain.model.Product;

import java.util.List;

/**
 * 최근 본 상품과 신간 상품 Response Dto
 *
 * @author 김선홍
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRecentResponseDto {

    private Long id;
    private String title;
    private String thumbnailFileUrl;
    private long sellingPrice;
    private int rate;
    private String publisher;
    private List<String> author;
    private Boolean isForcedOutOfStock;
    private Long quantity;

    public static ProductRecentResponseDto fromEntity(Product product, Long sellingPrice, int rate, String publisher, List<String> author) {
        return ProductRecentResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .sellingPrice(sellingPrice)
                .rate(rate)
                .isForcedOutOfStock(product.isForcedOutOfStock())
                .quantity(product.getQuantity())
                .author(author)
                .publisher(publisher)
                .thumbnailFileUrl(product.getThumbnailFile().getUrl())
                .build();
    }
}
