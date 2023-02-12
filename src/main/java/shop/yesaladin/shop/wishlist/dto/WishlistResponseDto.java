package shop.yesaladin.shop.wishlist.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 위시리스트 response DTO
 *
 * @author 김선홍
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponseDto {

    private Long id;
    private String title;
    private String thumbnailFileUrl;
    private long sellingPrice;
    private int rate;
    private String publisher;
    private List<String> author;
    private Boolean isForcedOutOfStock;
    private Long quantity;
    private LocalDateTime registeredDateTime;

    public static WishlistResponseDto fromEntity(
            Product product,
            String publisher,
            List<String> author,
            LocalDateTime registeredDateTime,
            Long sellingPrice,
            int rate
    ) {
        return WishlistResponseDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .thumbnailFileUrl(product.getThumbnailFile().getUrl())
                .sellingPrice(sellingPrice)
                .rate(rate)
                .author(author)
                .isForcedOutOfStock(product.isForcedOutOfStock())
                .publisher(publisher)
                .quantity(product.getQuantity())
                .registeredDateTime(registeredDateTime)
                .build();
    }
}
