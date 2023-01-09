package shop.yesaladin.shop.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ElasticConfiguration extends ElasticsearchConfiguration {
    @Value("${elastic.host}")
    String host;

    @Value("${elastic.port}")
    String port;

    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .build();
    }
}
