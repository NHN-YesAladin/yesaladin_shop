package shop.yesaladin.shop.publish.dto;

import lombok.*;
import shop.yesaladin.shop.publish.domain.model.Publisher;

/**
 * 출판사 데이터를 반환받는 DTO 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PublisherResponseDto {

    private Long id;
    private String name;

    public Publisher toEntity() {
        return Publisher.builder().id(id).name(name).build();
    }
}
