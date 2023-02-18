package shop.yesaladin.shop.product.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;

import java.util.List;

/**
 * 간단한 상품 정보를 응답하기 위한 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    // 기본 정보
    private Long id;
    private String title;
    private String thumbnailFileUrl;

    // 저자, 출판사
    private List<AuthorsResponseDto> authors;
    private PublisherResponseDto publisher;

    // 금액
    private long sellingPrice;
}
