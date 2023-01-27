package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;

import java.util.List;

public class DummyProductDetailResponseDto {

    public static ProductDetailResponseDto dummy() {
        List<String> authors = List.of("저자1");

        return new ProductDetailResponseDto(
                1L,
                "E-BOOK-URL",
                "제목",
                authors,
                "출판사",
                "THUMBNAIL-URL",
                10000L,
                9000L,
                10,
                1000,
                10,
                "2023-01-23",
                "0000000000001",
                false,
                "",
                "목차",
                "설명"
        );
    }

}
