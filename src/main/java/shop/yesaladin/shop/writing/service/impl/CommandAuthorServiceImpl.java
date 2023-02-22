package shop.yesaladin.shop.writing.service.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.CommandAuthorRepository;
import shop.yesaladin.shop.writing.domain.repository.QueryAuthorRepository;
import shop.yesaladin.shop.writing.dto.AuthorRequestDto;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.service.inter.CommandAuthorService;

/**
 * 저자 생성/수정/삭제를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandAuthorServiceImpl implements CommandAuthorService {

    private final CommandAuthorRepository commandAuthorRepository;
    private final QueryAuthorRepository queryAuthorRepository;
    private final QueryMemberService queryMemberService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public AuthorResponseDto create(AuthorRequestDto createDto) {
        Member member = getMember(createDto.getLoginId());

        Author author = Author.builder()
                .name(createDto.getName())
                .member(member)
                .build();

        commandAuthorRepository.save(author);

        return new AuthorResponseDto(
                author.getId(),
                author.getName(),
                author.getMember()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public AuthorResponseDto modify(Long id, AuthorRequestDto modifyDto) {
        Author author = queryAuthorRepository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.WRITING_AUTHOR_NOT_FOUND,
                        "Author is not found with id : " + id
                ));

        author.changeName(modifyDto.getName());
        author.changeMember(getMember(modifyDto.getLoginId()));

        Author savedAuthor = commandAuthorRepository.save(author);

        return new AuthorResponseDto(
                savedAuthor.getId(),
                savedAuthor.getName(),
                savedAuthor.getMember()
        );
    }

    /**
     * loginId에 해당하는 멤버 엔터티를 조회해 반환하며, 존재하지 않는 아이디인 경우 MemberNotFoundException를 던지고, loginId가 null인
     * 경우 멤버 엔터티를 null로 반환합니다.
     *
     * @param loginId 찾을 로그인 아이디
     * @return 조회한 멤버 엔터티
     * @author 이수정
     * @since 1.0
     */
    private Member getMember(String loginId) {
        if (Objects.nonNull(loginId) && !loginId.isBlank()) {
            return queryMemberService.findMemberByLoginId(loginId).toEntity();
        }
        return null;
    }
}
