package shop.yesaladin.shop.product.dummy;

import java.util.List;
import shop.yesaladin.shop.product.dto.ProductUpdateDto;

public class DummyProductUpdateDto {

    public static ProductUpdateDto dummy(String title) {
        List<Long> authors = List.of(1L);

        List<Long> tags = List.of(1L, 2L);
        List<Long> categories = List.of(1L, 2L);

        return new ProductUpdateDto(
                title,
                "목차",
                "상품 더미 입니다.",
                authors,
                1L,
                30000,
                15,
                true,
                3,
                true,
                "",
                false,
                true,
                100,
                "2000-06-08",
                3,
                "UUID.png",
                "2022-12-25T01:22:33",
                "UUID.pdf",
                "2023-01-17T00:23:17",
                "BESTSELLER",
                "SELLING_PRICE",
                tags,
                categories,
                false
        );
    }
}
