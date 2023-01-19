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
import shop.yesaladin.shop.common.dto.ErrorResponseDto;
import shop.yesaladin.shop.common.exception.CustomJsonProcessingException;
import shop.yesaladin.shop.file.exception.FileNotFoundException;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;
import shop.yesaladin.shop.member.exception.MemberRoleNotFoundException;
import shop.yesaladin.shop.point.exception.InvalidCodeParameterException;
import shop.yesaladin.shop.point.exception.OverPointUseException;
import shop.yesaladin.shop.product.exception.*;
import shop.yesaladin.shop.publish.exception.PublishNotFoundException;
import shop.yesaladin.shop.publish.exception.PublisherNotFoundException;
import shop.yesaladin.shop.tag.exception.TagNotFoundException;
import shop.yesaladin.shop.writing.exception.AuthorNotFoundException;
import shop.yesaladin.shop.writing.exception.WritingNotFoundException;

/**
 * 공용으로 사용하는 예외 처리
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {
            CategoryNotFoundException.class,
            MemberRoleNotFoundException.class,
            MemberNotFoundException.class,
            ProductNotFoundException.class,
            ProductSavingMethodCodeNotFoundException.class,
            ProductTypeCodeNotFoundException.class,
            SubscribeProductNotFoundException.class,
            TotalDiscountRateNotExistsException.class,
            PublisherNotFoundException.class,
            PublishNotFoundException.class,
            TagNotFoundException.class,
            AuthorNotFoundException.class,
            WritingNotFoundException.class,
            FileNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(Exception ex) {
        log.error("[NOT_FOUND] handleNotFoundException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            OverPointUseException.class,
            InvalidCodeParameterException.class,
            AlreadyDeletedProductException.class,
            AlreadyProductExistsException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleValidationException(Exception ex) {
        log.error("[BAD_REQUEST] handleValidationException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(value = {MemberProfileAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponseDto> handleAlreadyExistException(Exception ex) {
        log.error("[CONFLICT] handleAlreadyExistException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler({CustomJsonProcessingException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleJsonProcessingException(Exception ex) {
        log.error("[INTERNAL_SERVER_ERROR] handleJsonProcessingException", ex);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        log.error("[INTERNAL_SERVER_ERROR] handleException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
