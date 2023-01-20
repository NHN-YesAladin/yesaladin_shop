package shop.yesaladin.shop.writing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.writing.domain.model.Author;

/**
 * 저자 데이터를 반환받는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {

    private Long id;
    private String name;
    private Member member;

    /**
     * Dto를 바탕으로 저자 엔터티를 생성해 반환합니다.
     *
     * @return 생성된 저자 엔터티
     * @author 이수정
     * @since 1.0
     */
    public Author toEntity() {
        return Author.builder()
                .id(id)
                .name(name)
                .member(member)
                .build();
    }
}
