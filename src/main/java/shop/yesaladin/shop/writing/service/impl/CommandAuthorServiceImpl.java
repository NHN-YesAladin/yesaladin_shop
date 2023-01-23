package shop.yesaladin.shop.writing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.CommandAuthorRepository;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.service.inter.CommandAuthorService;

/**
 * 저자 생성/수정/삭제를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandAuthorServiceImpl implements CommandAuthorService {

    private final CommandAuthorRepository commandAuthorRepository;

    /**
     * 저자를 저장하고, 생성된 저자 Dto를 반환합니다.
     *
     * @param author 저장할 저자 엔터티
     * @return 생성된 저자 Dto
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public AuthorResponseDto register(Author author) {
        Author savedAuthor = commandAuthorRepository.save(author);

        return new AuthorResponseDto(
                savedAuthor.getId(),
                savedAuthor.getName(),
                savedAuthor.getMember()
        );
    }
}
