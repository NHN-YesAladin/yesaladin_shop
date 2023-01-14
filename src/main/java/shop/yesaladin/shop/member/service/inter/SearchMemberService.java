package shop.yesaladin.shop.member.service.inter;

import java.time.LocalDate;
import java.util.List;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.dto.SearchMemberManagerRequestDto;

/**
 * 엘라스틱 서치의 C,U,D 및 각 조건에 대한 검색을 위한 service interface
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchMemberService {

    /**
     * 엘라스틱 서치에 새로 회원가입한 회원을 등록하기 위한 메서드
     *
     * @param searchMemberManagerRequestDto 새로 회원가입한 회원의 정보
     * @return 등록된 정보
     * @author : 김선홍
     * @since : 1.0
     */
    SearchMemberManagerRequestDto saveNewMember(SearchMemberManagerRequestDto searchMemberManagerRequestDto);

    /**
     * 엘라스틱 서치에 회원의 정보를 수정하기 위한 메서드
     *
     * @param searchMemberManagerRequestDto 수정한 회원의 정보
     * @return 등록된 정보
     * @author : 김선홍
     * @since : 1.0
     */
    SearchMemberManagerRequestDto updateMember(SearchMemberManagerRequestDto searchMemberManagerRequestDto);

    /**
     * 엘라스틱 서치에 회원의 정보를 삭제하기 위한 메서드
     *
     * @param loginId 삭제할 회원의 loginId
     * @author : 김선홍
     * @since : 1.0
     */
    void deleteMember(String loginId);

    /**
     * 회원의 로그안 아이디로 검색하는 메서드
     *
     * @param loginId 검색할 회원의 로그인 아이디
     * @return 검색된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    SearchMemberManagerRequestDto searchByLoginId(String loginId);

    /**
     * 핸드폰 번호로 회원을 검색하는 메서드
     *
     * @param phone 검색할 회원의 핸드폰 번호
     * @return 검색된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    SearchMemberManagerRequestDto searchByPhone(String phone);

    /**
     * 회원의 닉네임으로 검색하는 메서드
     *
     * @param nickname 검색할 회원의 닉네임
     * @return 검색된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    SearchMemberManagerRequestDto searchByNickname(String nickname);

    /**
     * 회원의 이름으로 검색하는 메서드
     *
     * @param name 검색할 회원의 이름
     * @return 검색된 회원의 정보 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchMemberManagerRequestDto> searchByName(String name);

    /**
     * 회원가입한 날로 회원을 검색하는 메서드
     *
     * @param signUpDate 회원가입 날짜
     * @return 검색된 회원의 정보 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchMemberManagerRequestDto> searchBySignUpDate(LocalDate signUpDate);



}
