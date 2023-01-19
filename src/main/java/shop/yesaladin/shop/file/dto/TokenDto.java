package shop.yesaladin.shop.file.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Jackson으로 Json에서 토큰을 얻기 위해 사용하는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenDto {

    private String id;
}
