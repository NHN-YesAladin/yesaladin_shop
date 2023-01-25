package shop.yesaladin.shop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "storage-token")
public class ObjectStorageProperties {

    private String storageUrl;
    private String storageAccount;
    private String containerName;
    private String authUrl;
    private String tenantId;
    private String username;
    private String password;
}
