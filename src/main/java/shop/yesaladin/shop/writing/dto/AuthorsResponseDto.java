package shop.yesaladin.shop.writing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 저자 전체 조회를 하여 Dto로 반환합니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorsResponseDto {

    private Long id;
    private String name;
    private String loginId;

    /**
     * 집필을 바탕으로 저자 Dto를 만들어 반환합니다.
     *
     * @param writing 바탕이 될 집필 Dto
     * @return 저자 Dto
     * @author 이수정
     * @since 1.0
     */
    public static AuthorsResponseDto getAuthorFromWriting(WritingResponseDto writing) {
        return AuthorsResponseDto.builder()
                .id(writing.getAuthor().getId())
                .name(writing.getAuthor().getName())
                .loginId(Objects.isNull(writing.getAuthor().getMember()) ? null
                        : writing.getAuthor().getMember().getLoginId())
                .build();
    }
}
