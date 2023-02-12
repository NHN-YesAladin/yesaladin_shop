package shop.yesaladin.shop.wishlist.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 위시리스트 등록 결과 Dto
 *
 * @author 김선홍
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class WishlistSaveResponseDto {
    private Long productId;
    private LocalDateTime registeredDateTime;
}
