package shop.yesaladin.shop.product.dummy;

import java.util.ArrayList;
import java.util.List;
import shop.yesaladin.shop.product.dto.ProductCreateDto;

public class DummyProductCreateDto {
    public static ProductCreateDto dummy(String ISBN) {
        List<String> tags = new ArrayList<>(){};
        tags.add("아름다운");
        tags.add("슬픈");

        return new ProductCreateDto(
                ISBN,
                "더미",
                "목차",
                "상품 더미 입니다.",
                "테스트",
                "",
                "귈벗",
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
