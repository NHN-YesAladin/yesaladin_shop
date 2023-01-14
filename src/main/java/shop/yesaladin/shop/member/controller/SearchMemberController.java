package shop.yesaladin.shop.member.controller;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.member.dto.SearchMemberManagerRequestDto;
import shop.yesaladin.shop.member.service.inter.SearchMemberService;


/**
 * 회원을 로그인 아이디, 닉네임, 핸드폰번호, 이름, 회원가입날로 검색과 엘라스틱서치에서 생성, 수정, 삭제 해주는 Controller
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@RequestMapping("/v1/members/search")
@RestController
public class SearchMemberController {

    private final SearchMemberService searchMemberService;

    /**
     * 엘라스틱서치에 회원을 저장하는 메서드
     *
     * @param searchMemberManagerRequestDto 저장할 회원의 정보
     * @return 저장된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @PostMapping
    ResponseEntity<SearchMemberManagerRequestDto> saveMember(@Valid @RequestBody SearchMemberManagerRequestDto searchMemberManagerRequestDto) {
        return ResponseEntity.ok(searchMemberService.saveNewMember(searchMemberManagerRequestDto));
    }

    /**
     * 엘라스틱서치에 저장된 회원의 정보를 수정하는 메서드
     *
     * @param searchMemberManagerRequestDto 수정할 회원의 정보
     * @return 수정된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @PutMapping
    ResponseEntity<SearchMemberManagerRequestDto> updateMember(@Valid @RequestBody SearchMemberManagerRequestDto searchMemberManagerRequestDto) {
        return ResponseEntity.ok(searchMemberService.updateMember(searchMemberManagerRequestDto));
    }

    /**
     * 로그인 아이디로 회원을 삭제하는 메서드
     *
     * @param loginId 삭제할 회원의 로그인 아이디
     * @author : 김선홍
     * @since : 1.0
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{loginid}")
    void deleteMember(@PathVariable("loginid") String loginId) {
        searchMemberService.deleteMember(loginId);
    }

    /**
     * 로그인 아이디로 회원을 검색하는 메서드
     *
     * @param loginId 검색할 회원의 로그인 아이디
     * @return 검색된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/loginid/{loginid}")
    ResponseEntity<SearchMemberManagerRequestDto> searchByLoginId(@PathVariable("loginid") String loginId) {
        return ResponseEntity.ok(searchMemberService.searchByLoginId(loginId));
    }

    /**
     * 닉네임으로 회원을 검색하는 메서드
     *
     * @param nickname 검색할 회원의 닉네임
     * @return 검색된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/nickname/{nickname}")
    ResponseEntity<SearchMemberManagerRequestDto> searchByNickname(@PathVariable("nickname") String nickname) {
        return ResponseEntity.ok(searchMemberService.searchByNickname(nickname));
    }

    /**
     * 핸드폰번호로 회원을 검색하는 메서드
     *
     * @param phone 검색할 회원의 핸드폰 번호
     * @return 검색된 회원의 정보
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/phone/{phone}")
    ResponseEntity<SearchMemberManagerRequestDto> searchByPhone(@PathVariable("phone") String phone) {
        return ResponseEntity.ok(searchMemberService.searchByPhone(phone));
    }

    /**
     * 이름으로 회원을 검색하는 메서드
     *
     * @param name 검색할 회원의 이름
     * @return 검색된 회원의 정보 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/name/{name}")
    List<SearchMemberManagerRequestDto> searchByName(@PathVariable("name") String name) {
        return searchMemberService.searchByName(name);
    }

    /**
     * 회원가입날로 회원을 검색하는 메서드
     *
     * @param signUpDate 검색할 회원의 회원가입날
     * @return 검색된 회원의 정보 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @GetMapping("/signupdate/{signUpDate}")
    List<SearchMemberManagerRequestDto> searchBySignUpDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate signUpDate) {
        return searchMemberService.searchBySignUpDate(signUpDate);
    }
}
