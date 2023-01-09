package shop.yesaladin.shop.file.service.impl;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.domain.repository.CommandFileRepository;
import shop.yesaladin.shop.file.service.inter.CommandFileService;

/**
 * 파일 등록을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandFileServiceImpl implements CommandFileService {

    private final CommandFileRepository commandFileRepository;

    /**
     * 파일을 DB에 등록하고, 저장한 파일 객체를 리턴합니다.
     *
     * @param file 파일 엔터티
     * @return 저장된 파일 객체
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public File register(File file) {
        return commandFileRepository.save(file);
    }
}
