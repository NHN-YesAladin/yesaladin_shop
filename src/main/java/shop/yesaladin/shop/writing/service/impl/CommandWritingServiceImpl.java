package shop.yesaladin.shop.writing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.domain.repository.CommandWritingRepository;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;

import javax.transaction.Transactional;

/**
 * 집필 생성/수정/삭제를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandWritingServiceImpl implements CommandWritingService {

    private final CommandWritingRepository commandWritingRepository;

    /**
     * 집필을 저장하고, 생성된 집필 dto를 반환합니다.
     *
     * @param writing 저장할 집필 엔터티
     * @return 생성된 집필 dto
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public WritingResponseDto register(Writing writing) {
        Writing savedWriting = commandWritingRepository.save(writing);

        return new WritingResponseDto(
                savedWriting.getProduct(),
                savedWriting.getAuthor()
        );
    }
}
