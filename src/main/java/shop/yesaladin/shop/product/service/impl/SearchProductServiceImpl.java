package shop.yesaladin.shop.product.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.shop.file.exception.FileNotFoundException;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchProductRequestDto;
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
     * 카테고리 id로 상품을 검색하는 메서드
     *
     * @param id 검색하고 싶은 카테고리 id
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByCategoryId(
            Long id, int offset, int size
    ) {
        return searchProductRepository.searchProductsByCategoryId(id, offset, size);
    }

    /**
     * 카테고리 이름으로 상품을 검색하는 메서드
     *
     * @param name 검색하고 싶은 카테고리 이름
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByCategoryName(
            String name, int offset, int size
    ) {
        return searchProductRepository.searchProductsByCategoryName(name, offset, size);
    }

    /**
     * 상품 제목으로 상품을 검색하는 메서드
     *
     * @param title  검색하고 싶은 상품 제목
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 해당 이름의 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByProductTitle(
            String title, int offset, int size
    ) {
        return searchProductRepository.searchProductsByProductTitle(title, offset, size);
    }

    /**
     * 상품 내용으로 상품을 검색하는 메서드
     *
     * @param content 검색하고 싶은 상품 내용
     * @param offset  검색하고 싶은 페이지 위치
     * @param size    검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByProductContent(
            String content, int offset, int size
    ) {
        return searchProductRepository.searchProductsByProductContent(content, offset, size);
    }

    /**
     * 상품 isbn로 상품을 검색하는 메서드
     *
     * @param isbn   검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByProductISBN(
            String isbn, int offset, int size
    ) {
        return searchProductRepository.searchProductsByProductISBN(isbn, offset, size);
    }

    /**
     * 작가 이름으로 상품을 검색하는 메서드
     *
     * @param author 검색하고 싶은 작가 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByProductAuthor(
            String author, int offset, int size
    ) {
        return searchProductRepository.searchProductsByProductAuthor(author, offset, size);
    }

    /**
     * 출판사 이름으로 상품을 검색하는 메서드
     *
     * @param publisher 검색하고 싶은 출판사 이름
     * @param offset    검색하고 싶은 페이지 위치
     * @param size      검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByPublisher(
            String publisher, int offset, int size
    ) {
        return searchProductRepository.searchProductsByPublisher(publisher, offset, size);
    }

    /**
     * 태그 이름으로 상품을 검색하는 메서드
     *
     * @param tag    검색하고 싶은 태그 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 해당 태그의 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByTag(String tag, int offset, int size) {
        return searchProductRepository.searchProductsByTag(tag, offset, size);
    }
}
