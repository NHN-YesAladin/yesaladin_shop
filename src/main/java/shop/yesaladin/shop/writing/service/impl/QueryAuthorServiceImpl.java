package shop.yesaladin.shop.writing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.QueryAuthorRepository;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 저자 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryAuthorServiceImpl implements QueryAuthorService {

    private final QueryAuthorRepository queryAuthorRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public AuthorResponseDto findById(Long id) {
        Author author = queryAuthorRepository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.WRITING_AUTHOR_NOT_FOUND,
                        "Author is not found with id : " + id));

        return new AuthorResponseDto(author.getId(), author.getName(), author.getMember());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<AuthorsResponseDto> findAllForManager(Pageable pageable) {
        return getAuthorsResponseDtoList(queryAuthorRepository.findAllForManager(pageable));

    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<AuthorsResponseDto> findAllByLoginIdForManager(
            String loginId,
            Pageable pageable
    ) {
        return getAuthorsResponseDtoList(queryAuthorRepository.findAllByLoginIdForManager(loginId, pageable));

    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<AuthorsResponseDto> findAllByNameForManager(
            String name,
            Pageable pageable
    ) {
        return getAuthorsResponseDtoList(queryAuthorRepository.findAllByNameForManager(name, pageable));
    }

    private PaginatedResponseDto<AuthorsResponseDto> getAuthorsResponseDtoList(Page<Author> page) {
        List<AuthorsResponseDto> authors = new ArrayList<>();
        for (Author author : page.getContent()) {
            authors.add(AuthorsResponseDto.builder()
                    .id(author.getId())
                    .name(author.getName())
                    .loginId(Objects.isNull(author.getMember()) ? null : author.getMember().getLoginId())
                    .build()
            );
        }

        return PaginatedResponseDto.<AuthorsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(authors)
                .build();
    }
}
