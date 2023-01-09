package shop.yesaladin.shop.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * queryDsl을 사용하기 위해 JPAQueryFactory 를 Bean으로 등록하는 Spring Configuration 클래스
 *
 * @author 김홍대
 * @since 1.0
 */
@Configuration
public class QueryDslConfiguration {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
