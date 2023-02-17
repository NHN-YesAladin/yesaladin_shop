package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;
import shop.yesaladin.shop.product.service.inter.SearchProductService;

/**
 * 상품 검색 서비스 구현체
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@Service
public class SearchProductServiceImpl implements SearchProductService {

    private final SearchProductRepository searchProductRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByCategoryId(
            Long id, Pageable pageable
    ) {
        return searchProductRepository.searchProductsByCategoryId(id, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByCategoryName(
            String name, Pageable pageable
    ) {
        return searchProductRepository.searchProductsByCategoryName(name, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByProductTitle(
            String title, Pageable pageable
    ) {
        return searchProductRepository.searchProductsByProductTitle(title, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByProductContent(
            String content, Pageable pageable
    ) {
        return searchProductRepository.searchProductsByProductContent(content, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByProductISBN(
            String isbn, Pageable pageable
    ) {
        return searchProductRepository.searchProductsByProductISBN(isbn, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByProductAuthor(
            String author, Pageable pageable
    ) {
        return searchProductRepository.searchProductsByProductAuthor(author, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByPublisher(
            String publisher, Pageable pageable
    ) {
        return searchProductRepository.searchProductsByPublisher(publisher, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByTag(String tag, Pageable pageable) {
        return searchProductRepository.searchProductsByTag(tag, pageable);
    }
}
