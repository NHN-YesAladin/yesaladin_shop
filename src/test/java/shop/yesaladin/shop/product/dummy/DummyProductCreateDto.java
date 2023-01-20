package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.dto.ProductCreateDto;

import java.util.ArrayList;
import java.util.List;

public class DummyProductCreateDto {

    public static ProductCreateDto dummy(String ISBN) {
        List<Long> authors = new ArrayList<>() {
        };
        authors.add(1L);

        List<Long> tags = new ArrayList<>() {
        };
        tags.add(1L);
        tags.add(2L);

        return new ProductCreateDto(
                ISBN,
                "더미",
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
                tags
        );
    }

}
