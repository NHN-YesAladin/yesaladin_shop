package shop.yesaladin.shop.writing.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.CommandAuthorRepository;

/**
 * 저자 Repository 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaAuthorRepository extends Repository<Author, Long>,
        CommandAuthorRepository {

}
