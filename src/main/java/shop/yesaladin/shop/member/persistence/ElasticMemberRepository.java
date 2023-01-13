package shop.yesaladin.shop.member.persistence;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.domain.repository.SearchMemberRepository;

/**
 * 엘라스틱에 접근하는 회원 검색 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface ElasticMemberRepository extends SearchMemberRepository,
        ElasticsearchRepository<SearchedMember, Long> {
}
