package shop.yesaladin.shop.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.yesaladin.shop.product.domain.model.Product;

@Getter
@RequiredArgsConstructor
public class ProductTagCreateDto {

    private final Product product;
    private final long tagId;
}
