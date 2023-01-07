package shop.yesaladin.shop.file.domain.repository;

import shop.yesaladin.shop.file.domain.model.File;

/**
 * 파일 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandFileRepository {
    File save(File file);
}
