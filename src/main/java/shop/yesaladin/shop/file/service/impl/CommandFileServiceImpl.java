package shop.yesaladin.shop.file.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.domain.repository.CommandFileRepository;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.service.inter.CommandFileService;

import javax.transaction.Transactional;

/**
 * 파일 등록을 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandFileServiceImpl implements CommandFileService {

    private final CommandFileRepository commandFileRepository;

    /**
     * 파일을 DB에 등록하고, 저장한 파일 객체를 반환합니다.
     *
     * @param file 파일 엔터티
     * @return 저장된 파일 객체
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public FileResponseDto register(File file) {
        File savedFile = commandFileRepository.save(file);

        return new FileResponseDto(
                savedFile.getId(),
                savedFile.getUrl(),
                savedFile.getUploadDateTime()
        );
    }
}
