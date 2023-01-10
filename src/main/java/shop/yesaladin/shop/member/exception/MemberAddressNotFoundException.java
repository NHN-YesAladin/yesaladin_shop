package shop.yesaladin.shop.member.exception;

public class MemberAddressNotFoundException extends RuntimeException {

    public MemberAddressNotFoundException(Long addressId) {
        super("MemberAddress Not Found: " + addressId);
    }
}
