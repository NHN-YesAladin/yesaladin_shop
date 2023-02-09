package shop.yesaladin.shop.wishlist.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishlistSaveResponseDto {
    private Long productId;
    private LocalDateTime registeredDateTime;
}
