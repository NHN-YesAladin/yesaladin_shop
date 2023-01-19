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
 * 파일 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QueryFileServiceImpl implements QueryFileService {

    private final QueryFileRepository queryFileRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public FileResponseDto findById(Long id) {
        File file = queryFileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id));

        return new FileResponseDto(file.getId(), file.getUrl(), file.getUploadDateTime());
    }
}
