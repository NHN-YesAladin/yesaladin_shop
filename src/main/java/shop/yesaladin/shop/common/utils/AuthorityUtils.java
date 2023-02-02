package shop.yesaladin.shop.common.utils;

import org.springframework.security.core.userdetails.UserDetails;
import shop.yesaladin.shop.common.exception.InvalidAuthorityException;

public class AuthorityUtils {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static boolean isAuthorized(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .noneMatch(x -> x.getAuthority().equals(ROLE_ANONYMOUS));
    }

    public static boolean isMember(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_USER));
    }

    public static boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_ADMIN));
    }

    public static boolean isAnonymous(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_ANONYMOUS));
    }
}
