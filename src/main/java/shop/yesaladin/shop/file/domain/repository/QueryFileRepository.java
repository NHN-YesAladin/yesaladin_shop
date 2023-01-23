package shop.yesaladin.shop.file.domain.repository;

import shop.yesaladin.shop.file.domain.model.File;

import java.util.Optional;

/**
 * 파일 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryFileRepository {

    Optional<File> findById(Long id);
}
