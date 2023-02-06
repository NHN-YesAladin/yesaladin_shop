package shop.yesaladin.shop.product.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchedProductManagerResponseDto {

    List<SearchedProductManagerDto> products;
    Long count;
}
