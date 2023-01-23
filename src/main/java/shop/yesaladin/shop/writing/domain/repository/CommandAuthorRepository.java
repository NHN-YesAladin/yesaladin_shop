package shop.yesaladin.shop.writing.domain.repository;

import shop.yesaladin.shop.writing.domain.model.Author;

/**
 * 저자 등록/수정/삭제 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandAuthorRepository {

    Author save(Author author);
}
