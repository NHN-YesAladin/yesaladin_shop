package shop.yesaladin.shop.member.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.repository.CommandMemberAddressRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;

public interface JpaMemberAddressRepository extends Repository<MemberAddress, Long>,
        CommandMemberAddressRepository, QueryMemberAddressRepository {

}
