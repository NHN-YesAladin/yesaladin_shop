package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;

import java.util.List;

/**
 * 전체 조회 응답을 하기 위한 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsResponseDto {

    private long id;

    private String title;
    private List<AuthorsResponseDto> authors;
    private PublisherResponseDto publisher;
    private String publishedDate;
    private long sellingPrice;
    private int discountRate;

    private long quantity;
    private Boolean isSale;
    private Boolean isForcedOutOfStock;
    private Boolean isShown;
    private Boolean isDeleted;

    private String thumbnailFileUrl;
    private List<TagResponseDto> tags;
    private String ebookFileUrl;

    private Boolean isEbook;
    private Boolean isSubscribeProduct;
}
