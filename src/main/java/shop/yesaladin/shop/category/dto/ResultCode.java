package shop.yesaladin.shop.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * delete 성공시 httpMessageConverter 가 result에 담긴 결과를 반환하도록 하는 객체
 * 성공시 : Success
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultCode {

    private String result;
}
