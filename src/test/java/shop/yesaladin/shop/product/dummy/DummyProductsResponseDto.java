package shop.yesaladin.shop.product.dummy;

import java.util.List;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;

public class DummyProductsResponseDto {

    public static ProductsResponseDto dummy(Long id, boolean isSale, boolean isDeleted) {
        List<AuthorsResponseDto> authors = List.of(AuthorsResponseDto.builder()
                .id(id)
                .name("저자")
                .loginId("loginId")
                .build());

        List<TagResponseDto> tags = List.of(
                TagResponseDto.builder().id(1L).name("아름다운").build(),
                TagResponseDto.builder().id(2L).name("슬픈").build(),
                TagResponseDto.builder().id(3L).name("기쁜").build()
        );

        return ProductsResponseDto.builder()
                .id(id)
                .title("제목")
                .authors(authors)
                .publisher(PublisherResponseDto.builder().id(id).name("출판사").build())
                .publishedDate("2023-01-23")
                .sellingPrice(9000L)
                .discountRate(10)
                .quantity(100)
                .isSale(isSale) // true || false
                .isForcedOutOfStock(false)
                .isShown(true)
                .isDeleted(isDeleted) // false || true
                .thumbnailFileUrl("URL")
                .tags(tags)
                .ebookFileUrl(null)
                .isEbook(false)
                .isSubscribeProduct(false)
                .build();
    }
}
