package shop.yesaladin.shop.member.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.member.domain.model.SearchedMember;

/**
 * 검색 전용 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchMemberRepository {
    Optional<SearchedMember> findByLoginId(String loginId);
    Optional<SearchedMember> findByNickname(String nickname);
    Optional<SearchedMember> findByPhone(String phone);
    List<SearchedMember> findAllByName(String name);
    List<SearchedMember> findBySignUpDate(LocalDate sighUpDate);

    SearchedMember save(SearchedMember searchedMember);

    void deleteByLoginId(String loginId);
}
