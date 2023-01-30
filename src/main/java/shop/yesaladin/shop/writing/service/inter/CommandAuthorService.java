package shop.yesaladin.shop.writing.service.inter;

import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.dto.AuthorRequestDto;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;

/**
 * 저자 생성/수정/삭제를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandAuthorService {

    /**
     * 저자를 저장하고, 생성된 저자 dto를 반환합니다.
     *
     * @param author 저장할 저자 엔터티
     * @return 생성된 저자 dto
     * @author 이수정
     * @since 1.0
     */
    AuthorResponseDto register(Author author);

    /**
     * 저자를 생성하여 저장하고, 생성된 저자 dto를 반환합니다.
     *
     * @param createDto 생성할 저자의 정보를 가진 Dto
     * @author 이수정
     * @since 1.0
     */
    AuthorResponseDto create(AuthorRequestDto createDto);

    /**
     * 저자를 수정하고 수정된 저자 dto를 반환합니다.
     *
     * @param modifyDto 수정된 저자의 정보를 가진 Dto
     * @author 이수정
     * @since 1.0
     */
    AuthorResponseDto modify(Long id, AuthorRequestDto modifyDto);
}
