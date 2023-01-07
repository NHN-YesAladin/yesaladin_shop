package shop.yesaladin.shop.writing.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.domain.repository.CommandWritingRepository;

/**
 * 집필 repository 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaWritingRepository extends Repository<Writing, Long>,
        CommandWritingRepository {

}
