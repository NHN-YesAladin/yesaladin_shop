package shop.yesaladin.shop.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 최근 본 상품 요청 dto
 *
 * @author 김선홍
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecentViewProductRequestDto {
    private List<Long> totalIds;
    private List<Long> pageIds;
}
