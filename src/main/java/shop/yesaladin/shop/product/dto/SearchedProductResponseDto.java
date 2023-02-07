package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchedProductResponseDto {

    List<SearchedProductDto> products;
    Long count;
}
