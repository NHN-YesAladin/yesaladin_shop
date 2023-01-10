package shop.yesaladin.shop.writing.dto;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 집필 데이터를 반환받는 DTO 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WritingResponseDto {
    private Long id;
    private String authorName;
    private Product product;
    private Member member;
}
