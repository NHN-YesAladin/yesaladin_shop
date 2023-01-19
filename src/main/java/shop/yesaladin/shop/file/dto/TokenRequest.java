package shop.yesaladin.shop.file.dto;

import lombok.Data;

/**
 * curl -X POST -H 'Content-Type:application/json' \
 * https://api-identity.infrastructure.cloud.toast.com/v2.0/tokens \ -d '{"auth": {"tenantId":
 * "*****", "passwordCredentials": {"username": "*****", "password": "*****"}}}' <- 요청 본문
 * <p>
 * 요청 본문에 들어갈 내용을 담고 있는 객체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Data
public class TokenRequest {

    private Auth auth = new Auth();

    /**
     * tenantId : 프로젝트에 대응하는 테넌트 ID passwordCredentials : username, password 를 담는 객체
     *
     * @author 이수정
     * @since 1.0
     */
    @Data
    public class Auth {

        private String tenantId;
        private PasswordCredentials passwordCredentials = new PasswordCredentials();
    }

    /**
     * username : NHN Cloud 회원 ID(이메일 형식) password : API Endpoint 설정 대화 상자에서 저장한 비밀번호
     *
     * @author 이수정
     * @since 1.0
     */
    @Data
    public class PasswordCredentials {

        private String username;
        private String password;
    }
}
