package shop.yesaladin.shop.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchedProductResponseDto {

    List<SearchedProductDto> products;
    Long count;
}
