package shop.yesaladin.shop.file.service.inter;

/**
 * Object Storage 인증 토큰을 발급받기 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface StorageAuthService {

    /**
     * 만약 Redis에 미리 발급받은 인증 토큰이 존재한다면 Redis에서 토큰을 반환받고,
     * 존재하지 않는다면, Object Storage 인증 토큰을 Json 형태로 발급받아 파싱하여 반환합니다.
     *
     * @return 발급된 토근 Id
     * @author 이수정
     * @since 1.0
     */
    String getAuthToken();
}
