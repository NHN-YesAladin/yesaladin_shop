package shop.yesaladin.shop.file.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.domain.repository.CommandFileRepository;
import shop.yesaladin.shop.file.domain.repository.QueryFileRepository;

/**
 * 파일 repository 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaFileRepository extends Repository<File, Long>,
        CommandFileRepository, QueryFileRepository {

}
