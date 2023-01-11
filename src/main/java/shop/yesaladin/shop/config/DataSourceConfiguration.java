package shop.yesaladin.shop.config;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.shop.common.dto.SecretResponse;

/**
 * Spring Boot의 기본 DataSource인 hikari를 DBCP2로 바꾸기 위한 설정 파일입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Slf4j
@Configuration
public class DataSourceConfiguration {

    @Value("${yesaladin.secure.url}")
    private String url;

    @Value("${yesaladin.secure.db_username}")
    private String username;

    @Value("${yesaladin.secure.db_password}")
    private String password;

    @Value("${yesaladin.secure.db_url}")
    private String dbUrl;


    /**
     * NHN Secure Key Manager를 통해 받아온 민감 정보를 사용하여 datasource를 Bean으로 등록하여 관리합니다.
     * 현재는 임시적인 설정이며, Database Connection 설정은 팀원들과 협의 하에 수정 예정입니다.
     *
     * @param restTemplate NHN Secure Key Manager를 통해 민감 정보를 받아오는 Bean 입니다.
     * @return DataSource 설정 결과를 반환합니다.
     * @author : 송학현
     * @since : 1.0
     */
    @Bean
    public DataSource dataSource(RestTemplate restTemplate) {
        SecretResponse usernameResponse = restTemplate.getForObject(
                url + this.username,
                SecretResponse.class
        );

        SecretResponse passwordResponse = restTemplate.getForObject(
                url + this.password,
                SecretResponse.class
        );

        SecretResponse dbUrlResponse = restTemplate.getForObject(
                url + this.dbUrl,
                SecretResponse.class
        );

        String username = usernameResponse.getBody().getSecret();
        String password = passwordResponse.getBody().getSecret();
        String dbUrl = dbUrlResponse.getBody().getSecret();

        log.info("========= Secure Key Manager 에서 불러온 username={} =========", username);
        log.info("========= Secure Key Manager 에서 불러온 password={} =========", password);
        log.info("========= Secure Key Manager 에서 불러온 dbUrl={} =========", dbUrl);

        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(dbUrlResponse.getBody().getSecret())
                .username(usernameResponse.getBody().getSecret())
                .password(passwordResponse.getBody().getSecret())
                .build();
    }
}
