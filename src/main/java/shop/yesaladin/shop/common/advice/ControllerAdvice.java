package shop.yesaladin.shop.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;
import shop.yesaladin.shop.member.exception.MemberRoleNotFoundException;

/**
 * 공용으로 사용하는 예외 처리
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {CategoryNotFoundException.class, MemberRoleNotFoundException.class,
            MemberNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException(Exception ex) {
        log.error("[NOT_FOUND] handleNotFoundException", ex);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(Exception ex) {
        log.error("[BAD_REQUEST] handleValidationException", ex);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = {MemberProfileAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleAlreadyExistException(Exception ex) {
        log.error("[CONFLICT] handleAlreadyExistException", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("[INTERNAL_SERVER_ERROR] handleException", ex);
        return ResponseEntity.internalServerError().build();
    }
}
