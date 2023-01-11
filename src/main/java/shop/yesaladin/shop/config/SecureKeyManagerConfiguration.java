package shop.yesaladin.shop.config;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.shop.common.dto.SecretResponse;

@Slf4j
@Configuration
public class SecureKeyManagerConfiguration {

    @Value("${yesaladin.secure.url}")
    private String url;

    @Value("${yesaladin.secure.db_username}")
    private String username;

    @Value("${yesaladin.secure.db_password}")
    private String password;

    @Value("${yesaladin.secure.db_url}")
    private String dbUrl;

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
