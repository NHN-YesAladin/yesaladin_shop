package shop.yesaladin.shop.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;

/**
 * 요청에 대한 인증 정보를 처리하는 util 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorityUtils {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * 인증된 요청인지 검증하고, 해당 인증의 username을 반환합니다.
     *
     * @param authentication 인증
     * @param errorMessage   인증 실패시 로그에 남길 에러 메세지
     * @return 인증된 username
     * @throws shop.yesaladin.common.exception.ClientException 인증되지 않은 요청입니다.
     * @author 최예린
     * @since 1.0
     */
    public static String getAuthorizedUserName(Authentication authentication, String errorMessage) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!AuthorityUtils.isAuthorized(userDetails)) {
            throw new ClientException(ErrorCode.UNAUTHORIZED, errorMessage);
        }
        return userDetails.getUsername();
    }

    /**
     * 인증이 되지 않은 요청인지 검증합니다.
     *
     * @param authentication 인증
     * @throws shop.yesaladin.common.exception.ClientException 인증된 요청입니다.
     */
    public static void checkAnonymousClient(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!AuthorityUtils.isAnonymous(userDetails)) {
            throw new ClientException(ErrorCode.UNAUTHORIZED, "Unauthorized client.");
        }
    }

    /**
     * 인증된 요청인지 아닌지 반환합니다.
     *
     * @param userDetails 인증정보
     * @return 인증여부
     * @author 최예린
     * @since 1.0
     */
    public static boolean isAuthorized(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .noneMatch(x -> x.getAuthority().equals(ROLE_ANONYMOUS));
    }

    /**
     * 익명으로부터의 요청인지 아닌지 반환합니다.
     *
     * @param userDetails 인증정보
     * @return 익명 여부
     */
    private static boolean isAnonymous(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_ANONYMOUS));
    }

    /**
     * 회원의 요청인지 아닌지 반환합니다.
     *
     * @param userDetails 인증 정보
     * @return 회원 여부
     */
    public static boolean isMember(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_USER));
    }

    /**
     * 관리자의 요청인지 아닌지 반환합니다.
     *
     * @param authentication 인증 정보
     * @return 관리자 여부
     */
    public static boolean isAdmin(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_ADMIN));
    }
}
