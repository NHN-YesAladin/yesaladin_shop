package shop.yesaladin.shop.common.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 콩통 응답용 Dto 클래스입니다.
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@Builder
@RequiredArgsConstructor
public class ResponseDto<T> {

    private final boolean success;
    @JsonIgnore
    private final HttpStatus status;
    private final T data;
    private final List<String> errorMessages;

    @JsonGetter
    public int getStatus() {
        return this.status.value();
    }
}
