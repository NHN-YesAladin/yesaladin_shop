package shop.yesaladin.shop.writing.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.writing.domain.model.Author;

import java.util.List;
import java.util.Optional;

/**
 * 저자 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryAuthorRepository {

    Optional<Author> findById(Long id);

    List<Author> findAll();

    Page<Author> findAllForManager(Pageable pageable);
}
