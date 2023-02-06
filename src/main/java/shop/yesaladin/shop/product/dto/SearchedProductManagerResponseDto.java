package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SearchedProductManagerResponseDto {

    List<SearchedProductManagerDto> products;
    Long count;
}
