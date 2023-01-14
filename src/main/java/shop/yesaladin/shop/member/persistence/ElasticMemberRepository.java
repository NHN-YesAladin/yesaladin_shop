package shop.yesaladin.shop.member.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.domain.repository.SearchMemberRepository;
import shop.yesaladin.shop.member.dto.SearchMemberManagerRequestDto;

/**
 * 엘라스틱에 접근하는 회원 검색 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface ElasticMemberRepository extends ElasticsearchRepository<SearchedMember, Long>,
        SearchMemberRepository {
    /**
     * 로그인 아이디로 회원을 검색하는 메서드
     *
     * @param loginId 회원의 로그인 아이디
     * @return 검색된 회원
     * @author : 김선홍
     * @since : 1.0
     */
    Optional<SearchedMember> searchByLoginId(String loginId);

    /**
     * 닉네임으로 회원을 검색하는 메서드
     *
     * @param nickname 회원의 닉네임
     * @return 검색된 회원
     * @author : 김선홍
     * @since : 1.0
     */
    Optional<SearchedMember> searchByNickname(String nickname);

    /**
     * 핸드폰 번호로 회원을 검색하는 메서드
     *
     * @param phone 회원의 핸드폰 번호
     * @return 검색된 회원
     * @author : 김선홍
     * @since : 1.0
     */
    Optional<SearchedMember> searchByPhone(String phone);

    /**
     * 이름으로 회원을 검색하는 메서드
     *
     * @param name 회원의 이름
     * @return 검색된 회원 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedMember> searchAllByName(String name);

    /**
     * 회원가입날로 검색하는 메서드
     *
     * @param sighUpDate 회원의 가입날
     * @return 검색된 회원 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedMember> searchBySignUpDate(LocalDate sighUpDate);

    /**
     * 회원의 loginId로 회원을 삭제하는 메서드
     *
     * @param loginId 삭제할 회원의 로그인 아이디
     * @author : 김선홍
     *
     * @since : 1.0
     */
    void deleteByLoginId(String loginId);
}
