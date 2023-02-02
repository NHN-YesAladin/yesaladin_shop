package shop.yesaladin.shop.common.advice;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;
import shop.yesaladin.shop.common.dto.ErrorResponseDto;
import shop.yesaladin.shop.common.exception.CommonException;
import shop.yesaladin.shop.common.exception.CustomJsonProcessingException;
import shop.yesaladin.shop.common.exception.InvalidAuthorityException;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.file.exception.FileNotFoundException;
import shop.yesaladin.shop.member.exception.AlreadyBlockedMemberException;
import shop.yesaladin.shop.member.exception.AlreadyDeletedAddressException;
import shop.yesaladin.shop.member.exception.AlreadyRegisteredUpToLimit;
import shop.yesaladin.shop.member.exception.AlreadyUnblockedMemberException;
import shop.yesaladin.shop.member.exception.MemberAddressNotFoundException;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;
import shop.yesaladin.shop.member.exception.MemberRoleNotFoundException;
import shop.yesaladin.shop.product.exception.AlreadyDeletedProductException;
import shop.yesaladin.shop.product.exception.ProductAlreadyExistsException;
import shop.yesaladin.shop.product.exception.ProductNotFoundException;
import shop.yesaladin.shop.product.exception.ProductSavingMethodCodeNotFoundException;
import shop.yesaladin.shop.product.exception.ProductTypeCodeNotFoundException;
import shop.yesaladin.shop.product.exception.SubscribeProductNotFoundException;
import shop.yesaladin.shop.product.exception.TotalDiscountRateNotExistsException;
import shop.yesaladin.shop.publish.exception.PublishNotFoundException;
import shop.yesaladin.shop.publish.exception.PublisherAlreadyExistsException;
import shop.yesaladin.shop.publish.exception.PublisherNotFoundException;
import shop.yesaladin.shop.tag.exception.TagAlreadyExistsException;
import shop.yesaladin.shop.tag.exception.TagNotFoundException;
import shop.yesaladin.shop.writing.exception.AuthorNotFoundException;
import shop.yesaladin.shop.writing.exception.WritingNotFoundException;

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
            MemberRoleNotFoundException.class,
            MemberNotFoundException.class,
            MemberAddressNotFoundException.class,
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
            InvalidPeriodConditionException.class,
            AlreadyDeletedProductException.class,
            AlreadyBlockedMemberException.class,
            AlreadyUnblockedMemberException.class,
            AlreadyDeletedAddressException.class,
            AlreadyRegisteredUpToLimit.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleValidationException(Exception ex) {
        log.error("[BAD_REQUEST] handleValidationException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidAuthorityException.class)
    public ResponseEntity<ResponseDto<Object>> handleUnauthorizedException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseDto.builder()
                        .success(false)
                        .status(HttpStatus.UNAUTHORIZED)
                        .errorMessages(List.of("접근권한이 없습니다."))
                        .build()
        );
    }

    @ExceptionHandler(value = {
            MemberProfileAlreadyExistException.class,
            ProductAlreadyExistsException.class,
            PublisherAlreadyExistsException.class,
            TagAlreadyExistsException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponseDto> handleAlreadyExistException(Exception ex) {
        log.error("[CONFLICT] handleAlreadyExistException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler({CustomJsonProcessingException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> handleJsonProcessingException(Exception ex) {
        log.error("[INTERNAL_SERVER_ERROR] handleJsonProcessingException", ex);
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * 에러를 반환합니다.
     *
     * @param e 에러
     * @return 에러
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ResponseDto<Object>> handleCommonException(CommonException e) {
        log.error(
                "[{}] handleCommonException : {}",
                e.getErrorCode().getResponseStatus(),
                e.getMessage()
        );
        ResponseDto<Object> response = ResponseDto.builder()
                .success(false)
                .status(e.getErrorCode().getResponseStatus())
                .errorMessages(
                        List.of(e.getMessage()))
                .build();

        return ResponseEntity.status(e.getErrorCode().getResponseStatus()).body(response);
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
