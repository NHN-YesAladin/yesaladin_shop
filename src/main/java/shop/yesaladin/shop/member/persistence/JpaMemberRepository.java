package shop.yesaladin.shop.member.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 회원 테이블에 JPA로 접근 가능한 인터페이스 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
public interface JpaMemberRepository extends Repository<Member, Long> {

}
