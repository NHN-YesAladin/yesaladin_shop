package shop.yesaladin.shop.file.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.domain.repository.QueryFileRepository;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.exception.FileNotFoundException;
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
     * Id로 파일을 조회해 반환합니다.
     *
     * @param id 찾고자하는 파일의 id
     * @return 찾은 파일 엔터티
     * @author 이수정
     * @since 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public FileResponseDto findById(Long id) {
        File file = queryFileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id));

        return new FileResponseDto(file.getId(), file.getUrl(), file.getUploadDateTime());
    }

//    /**
//     * url로 파일을 조회해 반환합니다.
//     *
//     * @param url 찾고자 하는 파일의 url
//     * @return 찾은 파일 엔터티
//     * @author 이수정
//     * @since 1.0
//     */
//    @Transactional(readOnly = true)
//    @Override
//    public FileResponseDto findByUrl(String url) {
//        File file = queryFileRepository.findByUrl(url)
//                .orElseThrow(() -> new FileNotFoundException(url));
//
//        return new FileResponseDto(file.getId(), file.getUrl(), file.getUploadDateTime());
//    }
}
