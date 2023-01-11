package shop.yesaladin.shop.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Web에 관련된 전반적인 설정을 관리합니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Slf4j
@Configuration
public class WebConfiguration {

    /**
     * NHN Secure Manager를 통해 민감정보를 받습니다.
     * yesaladin.p12 인증서를 통해 HTTPS 요청을 보냅니다.
     *
     * @return DataSource
     * @throws IOException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     *
     * @author : 송학현
     * @since : 1.0
     */
    @Bean
    public RestTemplate restTemplate() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");

        String file = getClass().getClassLoader().getResource("yesaladin.p12").getFile();

        clientStore.load(new FileInputStream(file), "yesaladin-password".toCharArray());

        SSLContext sslContext = SSLContextBuilder.create()
                .setProtocol("TLS")
                .loadKeyMaterial(clientStore, "yesaladin-password".toCharArray())
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(requestFactory);
    }
}
