package shop.yesaladin.shop.writing.service.impl;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.domain.repository.CommandWritingRepository;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;

/**
 * 집필 생성을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandWritingServiceImpl implements CommandWritingService {

    private final CommandWritingRepository commandWritingRepository;

    /**
     * 집필을 생성하고 집필 테이블에 저장합니다. 생성된 집필 객체를 리턴합니다.
     *
     * @param authorName 저자명
     * @param product    저자가 쓴 책 객체
     * @param member     저자가 회원이라면 회원 객체
     * @return 생성된 집필 객체
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public WritingResponseDto create(String authorName, Product product, Member member) {
        Writing writing = Writing.builder()
                .authorName(authorName)
                .product(product)
                .member(member)
                .build();

        Writing savedWriting = commandWritingRepository.save(writing);

        return new WritingResponseDto(
                savedWriting.getId(),
                savedWriting.getAuthorName(),
                savedWriting.getProduct(),
                savedWriting.getMember()
        );
    }
}
