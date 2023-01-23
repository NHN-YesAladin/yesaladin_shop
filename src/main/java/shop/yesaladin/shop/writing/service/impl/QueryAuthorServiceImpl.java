package shop.yesaladin.shop.writing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.QueryAuthorRepository;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.exception.AuthorNotFoundException;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 저자 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QueryAuthorServiceImpl implements QueryAuthorService {

    private final QueryAuthorRepository queryAuthorRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public AuthorResponseDto findById(Long id) {
        Author author = queryAuthorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        return new AuthorResponseDto(author.getId(), author.getName(), author.getMember());
    }


    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<AuthorsResponseDto> findAll() {
        return queryAuthorRepository.findAll().stream()
                .map(author -> new AuthorsResponseDto(author.getId(), author.getName(), Objects.isNull(author.getMember()) ? null : author.getMember().getLoginId()))
                .collect(Collectors.toList());
    }
}
