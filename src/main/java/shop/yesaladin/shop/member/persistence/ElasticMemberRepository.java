package shop.yesaladin.shop.member.persistence;
import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.domain.repository.SearchMemberRepository;

public interface ElasticMemberRepository extends Repository<SearchedMember, Long>, SearchMemberRepository {

}
