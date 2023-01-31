package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.product.dto.ProductsResponseDto;

import java.util.List;

public class DummyProductsResponseDto {

    public static ProductsResponseDto dummy(Long id) {
        List<String> authors = List.of("저자1");

        List<String> tags = List.of("아름다운", "슬픈", "행복한");

        return new ProductsResponseDto(
                id,
                "제목",
                authors,
                "출판사",
                "2023-01-23",
                9000L,
                10,
                100,
                true,
                false,
                true,
                false,
                "URL",
                tags,
                null
        );
    }

    public static ProductsResponseDto deletedDummy(Long id) {
        List<String> authors = List.of("저자1");

        List<String> tags = List.of("아름다운", "슬픈", "행복한");

        return new ProductsResponseDto(
                id,
                "제목",
                authors,
                "출판사",
                "2023-01-23",
                9000L,
                10,
                100,
                false,
                false,
                true,
                true,
                "URL",
                tags,
                null
        );
    }
}
