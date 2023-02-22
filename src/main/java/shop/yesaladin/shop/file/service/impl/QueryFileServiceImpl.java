package shop.yesaladin.shop.file.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
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
@RequiredArgsConstructor
@Service
public class QueryFileServiceImpl implements QueryFileService {

    private final QueryFileRepository queryFileRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public FileResponseDto findById(Long id) {
        File file = queryFileRepository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.FILE_NOT_FOUND,
                        "File is not found with id : " + id
                ));

        return new FileResponseDto(file.getId(), file.getUrl(), file.getUploadDateTime());
    }
}
