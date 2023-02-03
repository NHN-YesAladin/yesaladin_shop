package shop.yesaladin.shop.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;

public class AuthorityUtils {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static String getAuthorizedUserName(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!AuthorityUtils.isAuthorized(userDetails)) {
            throw new ClientException(ErrorCode.UNAUTHORIZED, "Unauthorized client.");
        }
        return userDetails.getUsername();
    }

    public static void checkAnonymousClient(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!AuthorityUtils.isAnonymous(userDetails)) {
            throw new ClientException(ErrorCode.UNAUTHORIZED, "Unauthorized client.");
        }
    }

    public static boolean isAuthorized(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .noneMatch(x -> x.getAuthority().equals(ROLE_ANONYMOUS));
    }

    private static boolean isAnonymous(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_ANONYMOUS));
    }

    public static boolean isMember(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_USER));
    }

    public static boolean isAdmin(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_ADMIN));
    }
}
