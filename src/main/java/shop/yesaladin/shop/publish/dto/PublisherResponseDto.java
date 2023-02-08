package shop.yesaladin.shop.publish.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.publish.domain.model.Publisher;

/**
 * 출판사 조회 데이터를 반환받는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherResponseDto {

    private Long id;
    private String name;

    /**
     * Dto를 바탕으로 출판사 엔터티를 생성해 반환합니다.
     *
     * @return 출판사 엔터티
     * @author 이수정
     * @since 1.0
     */
    public Publisher toEntity() {
        return Publisher.builder().id(id).name(name).build();
    }

    /**
     * 출판을 바탕으로 출판사 Dto를 만들어 반환합니다.
     *
     * @param publish 바탕이 될 출판 Dto
     * @return 출판사 Dto
     * @author 이수정
     * @since 1.0
     */
    public static PublisherResponseDto getPublisherFromPublish(PublishResponseDto publish) {
        return PublisherResponseDto.builder()
                .id(publish.getPublisher().getId())
                .name(publish.getPublisher().getName())
                .build();
    }
}
