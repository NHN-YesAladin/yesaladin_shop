package shop.yesaladin.shop.file.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.domain.repository.QueryFileRepository;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.service.inter.QueryFileService;

/**
 * 파일 조회를 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QueryFileServiceImpl implements QueryFileService {

    private final QueryFileRepository queryFileRepository;

    /**
     * 파일이름으로 이미 저장되어있는 파일인지 확인하여, 존재한다면 파일 엔터티를 반환, 존재하지 않는다면 null을 반환합니다.
     *
     * @param name 찾고자 하는 파일의 이름
     * @return 찾은 파일 엔터티 or null
     * @author 이수정
     * @since 1.0
     */
    @Override
    public FileResponseDto findByName(String name) {
        File file = queryFileRepository.findByName(name).orElse(null);

        if (file != null) {
            return new FileResponseDto(file.getId(), file.getName(), file.getUploadDateTime());
        } else {
            return null;
        }
    }
}
