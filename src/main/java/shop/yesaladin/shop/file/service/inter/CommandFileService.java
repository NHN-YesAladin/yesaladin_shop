package shop.yesaladin.shop.file.service.inter;

import shop.yesaladin.shop.file.domain.model.File;

/**
 * 파일 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandFileService {
    File register(File file);
}
