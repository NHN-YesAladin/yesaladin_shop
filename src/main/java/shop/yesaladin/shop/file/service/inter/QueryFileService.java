package shop.yesaladin.shop.file.service.inter;

import shop.yesaladin.shop.file.domain.model.File;

/**
 * 파일 조회를 위한 Service Interface 입니다. 사
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryFileService {

    File findByName(String name);
}
