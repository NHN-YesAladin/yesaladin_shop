package shop.yesaladin.shop.member.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.domain.repository.SearchMemberRepository;
import shop.yesaladin.shop.member.dto.SearchMemberManagerRequestDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;
import shop.yesaladin.shop.member.service.inter.SearchMemberService;

/**
 * 엘라스틱 서치의 C,U,D 및 각 조건에 대한 검색을 위한 service 구현체
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class SearchMemberServiceImpl implements SearchMemberService {

    private final SearchMemberRepository searchMemberRepository;

    /**
     * 엘라스틱 서치에 회원을 저장하는 메서드
     *
     * @param searchMemberManagerRequestDto 새로 회원가입한 회원의 정보
     * @return 저장된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchMemberManagerRequestDto saveNewMember(SearchMemberManagerRequestDto searchMemberManagerRequestDto) {
        if (checkLoginIdDuplication(searchMemberManagerRequestDto)
                || checkNicknameDuplication(searchMemberManagerRequestDto) || checkPhoneDuplication(
                searchMemberManagerRequestDto)) {
            throw new MemberProfileAlreadyExistException(searchMemberManagerRequestDto.toString());
        }
        return searchMemberRepository.save(searchMemberManagerRequestDto.toSearchedMember())
                .toDto();
    }

    /**
     * 엘라스틱서치에 저장된 회원의 정보를 수정하는 메서드
     *
     * @param searchMemberManagerRequestDto 수정한 회원의 정보
     * @return 수정된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchMemberManagerRequestDto updateMember(SearchMemberManagerRequestDto searchMemberManagerRequestDto) {
        if (checkUpdateNickname(searchMemberManagerRequestDto) || checkUpdatePhone(
                searchMemberManagerRequestDto)) {
            throw new MemberProfileAlreadyExistException(searchMemberManagerRequestDto.toString());
        }
        return searchMemberRepository.save(searchMemberManagerRequestDto.toSearchedMember())
                .toDto();
    }

    /**
     * 엘라스틱서치에 저장된 회원의 정보를 삭제하는 메서드
     *
     * @param loginId 삭제할 회원의 로그인 아이디
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public void deleteMember(String loginId) {
        searchMemberRepository.deleteByLoginId(loginId);
    }

    /**
     * 로그인 아이디로 회원을 검색하는 메서드
     *
     * @param loginId 검색할 회원의 로그인 아이디
     * @return 검색된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchMemberManagerRequestDto searchByLoginId(String loginId) {
        return searchMemberRepository.searchByLoginId(loginId).orElseThrow(() ->
                new MemberNotFoundException(loginId)).toDto();
    }

    /**
     * 핸드폰 번호로 회원을 검색하는 메서드
     *
     * @param phone 검색할 회원의 핸드폰 번호
     * @return 검색된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchMemberManagerRequestDto searchByPhone(String phone) {
        return searchMemberRepository.searchByPhone(phone).orElseThrow(() ->
                new MemberNotFoundException(phone)).toDto();
    }

    /**
     * 닉네임으로 회원을 검색하는 메서드
     *
     * @param nickname 검색할 회원의 닉네임
     * @return 검색된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchMemberManagerRequestDto searchByNickname(String nickname) {
        return searchMemberRepository.searchByNickname(nickname).orElseThrow(() ->
                new MemberNotFoundException(nickname)).toDto();
    }

    /**
     * 회원의 이름으로 회원을 검색하는 메서드
     *
     * @param name 검색할 회원의 이름
     * @return 검색된 회원의 정보 리스트
     */
    @Override
    public List<SearchMemberManagerRequestDto> searchByName(String name) {
        return searchMemberRepository.searchAllByName(name)
                .stream()
                .map(SearchedMember::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 회원가입날로 회원을 검색하는 메서드
     *
     * @param signUpDate 검색할 회원가입 날짜
     * @return 검색된 회원의 정보 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchMemberManagerRequestDto> searchBySignUpDate(LocalDate signUpDate) {
        return searchMemberRepository.searchBySignUpDate(signUpDate)
                .stream()
                .map(SearchedMember::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 회원가입에서 로그인 아이디 중복을 확인하는 메서드
     *
     * @param searchMemberManagerRequestDto 새로 등록할 회원의 정보
     * @return 누군가 사용하고 있는 로그인아이디면 true or false
     * @author : 김선홍
     * @since : 1.0
     */
    boolean checkLoginIdDuplication(SearchMemberManagerRequestDto searchMemberManagerRequestDto) {
        return searchMemberRepository.searchByLoginId(searchMemberManagerRequestDto.getLoginId())
                .isPresent();
    }

    /**
     * 회원가입에서 닉네임 중복을 확인하는 메서드
     *
     * @param searchMemberManagerRequestDto 새로 등록할 회원의 정보
     * @return 누군가 사용하고 있는 닉네임이면 true or false
     */
    boolean checkNicknameDuplication(SearchMemberManagerRequestDto searchMemberManagerRequestDto) {
        return searchMemberRepository.searchByNickname(searchMemberManagerRequestDto.getNickname())
                .isPresent();
    }

    /**
     * 회원가입에서 핸드폰 번호 중복을 확인하는 메서드
     *
     * @param searchMemberManagerRequestDto 새로 등록할 회원의 정보
     * @return 누군가 사용하고 있는 핸드폰 번호면 true or false
     * @author : 김선홍
     * @since : 1.0
     */
    boolean checkPhoneDuplication(SearchMemberManagerRequestDto searchMemberManagerRequestDto) {
        return searchMemberRepository.searchByPhone(searchMemberManagerRequestDto.getPhone())
                .isPresent();
    }

    /**
     * 회원정보 수정에서 해당 수정할 닉네임을 본인이 아닌 다른 누군가 사용하고 있는지 확인하는 메서드
     *
     * @param searchMemberManagerRequestDto 수정할 회원의 정보
     * @return 다른 누군가 닉네임을 사용하고 있다면 true or false
     * @author : 김선홍
     * @since : 1.0
     */
    boolean checkUpdateNickname(SearchMemberManagerRequestDto searchMemberManagerRequestDto) {
        Optional<SearchedMember> searchedMember = searchMemberRepository.searchByNickname(
                searchMemberManagerRequestDto.getNickname());
        return searchedMember.isPresent() && !Objects.equals(
                searchedMember.get().getId(),
                searchMemberManagerRequestDto.getId()
        );
    }

    /**
     * 회원정보 수정에서 해당 수정할 핸드폰 번호를 본인이 아닌 다른 누군가 사용하고 있는지 확인하는 메서드
     *
     * @param searchMemberManagerRequestDto 수정할 회원의 정보
     * @return 다른 누군가 핸드폰 번호를 사용하고 있다면 true or false
     * @author : 김선홍
     * @since : 1.0
     */
    boolean checkUpdatePhone(SearchMemberManagerRequestDto searchMemberManagerRequestDto) {
        Optional<SearchedMember> searchedMember = searchMemberRepository.searchByPhone(
                searchMemberManagerRequestDto.getPhone());
        return searchedMember.isPresent() && !Objects.equals(
                searchedMember.get().getId(),
                searchMemberManagerRequestDto.getId()
        );
    }
}
