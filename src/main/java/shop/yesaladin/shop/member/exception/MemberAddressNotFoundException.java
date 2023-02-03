package shop.yesaladin.shop.member.exception;

import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.shop.common.exception.CommonException;

public class MemberAddressNotFoundException extends CommonException {

    public MemberAddressNotFoundException() {
        super(ErrorCode.ADDRESS_NOT_FOUND);
    }
}
