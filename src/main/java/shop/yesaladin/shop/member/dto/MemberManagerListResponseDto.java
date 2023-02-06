package shop.yesaladin.shop.member.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원의 name 과 signUpDate 로 검색할 때 페이지 처리를 위한 dto
 *
 * @author 김선홍
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberManagerListResponseDto {

    private Long count;
    private List<MemberManagerResponseDto> memberManagerResponseDtoList;
}
