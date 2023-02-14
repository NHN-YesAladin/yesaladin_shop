package shop.yesaladin.shop.product.dummy;

import java.util.List;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;

public class DummyProductDetailResponseDto {

    public static ProductDetailResponseDto dummy() {
        List<AuthorsResponseDto> authors = List.of(
                AuthorsResponseDto.builder().id(1L).name("저자").loginId("loginId").build()
        );

        List<TagResponseDto> tags = List.of(
                TagResponseDto.builder().id(1L).name("아름다운").build(),
                TagResponseDto.builder().id(2L).name("슬픈").build(),
                TagResponseDto.builder().id(3L).name("기쁜").build()
        );

        List<CategoryResponseDto> categories = List.of(
                CategoryResponseDto.builder()
                        .id(1L)
                        .name("어린이")
                        .isShown(true)
                        .order(1)
                        .parentId(2L)
                        .parentName("국내도서").build()
        );

        return ProductDetailResponseDto.builder()
                .id(1L)
                .isbn("0000000000001")
                .title("제목")
                .contents("목차")
                .description("설명")
                .publishedDate("2023-01-23")
                .thumbnailFileUrl("THUMBNAIL_URL")
                .authors(authors)
                .publisher(PublisherResponseDto.builder().id(1L).name("출판사").build())
                .tags(tags)
                .categories(categories)
                .actualPrice(10000L)
                .sellingPrice(9000L)
                .discountRate(10)
                .pointPrice(1000)
                .pointRate(10)
                .isEbook(false)
                .isSubscriptionAvailable(false)
                .issn("11111111")
                .onSale(true).build();
    }

}
