package shop.yesaladin.shop.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Json 파싱 시 발생하는 JsonProcessingException 예외를 핸들링하기 위한 예외 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class CustomJsonProcessingException extends RuntimeException {

    public CustomJsonProcessingException(JsonProcessingException e) {
        super(e);
    }
}
