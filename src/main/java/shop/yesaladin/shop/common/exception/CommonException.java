package shop.yesaladin.shop.common.exception;

import lombok.Getter;
import shop.yesaladin.common.code.ErrorCode;

@Getter
public class CommonException extends RuntimeException {

    private final ErrorCode errorCode;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getDisplayName());

        this.errorCode = errorCode;
    }
}
