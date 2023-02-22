package shop.yesaladin.shop.order.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;

/**
 * 베스트셀러 정보를 전달하기 위한 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BestsellerResponseDto {

    // 상품 ID
    private long id;
    // 상품 제목
    private String title;
    // url
    private String thumbnailFileUrl;
    // 저자
    private List<AuthorsResponseDto> authors;
    // 출판사
    private PublisherResponseDto publisher;
    // 판매가
    private long sellingPrice;
}
