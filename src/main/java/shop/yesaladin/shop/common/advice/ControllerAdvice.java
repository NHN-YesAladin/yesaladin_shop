package shop.yesaladin.shop.common.advice;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;
import shop.yesaladin.shop.common.dto.ErrorResponseDto;
import shop.yesaladin.shop.common.exception.CustomJsonProcessingException;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.product.exception.ProductNotFoundException;

/**
 * 공용으로 사용하는 예외 처리
 *
 * @author 배수한
 * @author 최예린
 * @since 1.0
 */

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {
            CategoryNotFoundException.class,
            ProductNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(Exception ex) {
        log.error("[NOT_FOUND] handleNotFoundException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            InvalidPeriodConditionException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleValidationException(Exception ex) {
        log.error("[BAD_REQUEST] handleValidationException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({CustomJsonProcessingException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> handleJsonProcessingException(Exception ex) {
        log.error("[INTERNAL_SERVER_ERROR] handleJsonProcessingException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDto<Object>> handleAccessDeniedException(Exception e) {
        ResponseDto<Object> response = ResponseDto.builder()
                .success(false)
                .status(HttpStatus.FORBIDDEN)
                .errorMessages(
                        List.of(ErrorCode.FORBIDDEN.getDisplayName()))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ResponseDto<Object>> handleClientException(ClientException e) {
        log.error("[{}] ClientException : {}", e.getResponseStatus(), e.getMessage());

        ResponseDto<Object> response = ResponseDto.builder()
                .success(false)
                .status(e.getResponseStatus())
                .errorMessages(
                        List.of(e.getErrorCode().getDisplayName()))
                .build();
        return ResponseEntity.status(e.getResponseStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        log.error("[INTERNAL_SERVER_ERROR] handleException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
