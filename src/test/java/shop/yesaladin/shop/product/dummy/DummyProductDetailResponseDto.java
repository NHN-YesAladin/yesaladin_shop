package shop.yesaladin.shop.product.dummy;

import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;

import java.util.List;

public class DummyProductDetailResponseDto {

    public static ProductDetailResponseDto dummy() {
        List<String> authors = List.of("저자1");

        List<CategoryResponseDto> categories = List.of(
                CategoryResponseDto.builder()
                        .id(1L)
                        .name("어린이")
                        .isShown(true)
                        .order(1)
                        .parentId(2L)
                        .parentName("국내도서").build()
        );

        return new ProductDetailResponseDto(
                1L,
                false,
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
                "설명",
                true,
                categories
        );
    }

}
