package shop.yesaladin.shop.product.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;

/**
 * 상품 싱세 정보를 응답하기 위한 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDto {

    // 기본 정보
    private Long id;
    private String isbn;
    private String title;
    private String contents;
    private String description;
    private String publishedDate;
    private String thumbnailFileUrl;

    // 저자, 출판사, 태그, 카테고리
    private List<AuthorsResponseDto> authors;
    private PublisherResponseDto publisher;
    private List<TagResponseDto> tags;
    private List<CategoryResponseDto> categories;

    // 금액
    private long actualPrice;
    private long sellingPrice;
    private int discountRate;
    private long pointPrice;
    private int pointRate;

    // E-Book
    private Boolean isEbook;

    // 구독
    private Boolean isSubscriptionAvailable;
    private String issn;

    // 판매여부 관련
    private boolean onSale;
}
