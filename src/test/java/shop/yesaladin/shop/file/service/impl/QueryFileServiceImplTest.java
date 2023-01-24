package shop.yesaladin.shop.file.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.domain.repository.QueryFileRepository;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.exception.FileNotFoundException;
import shop.yesaladin.shop.file.service.inter.QueryFileService;
import shop.yesaladin.shop.product.dummy.DummyFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class QueryFileServiceImplTest {

    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private QueryFileService service;
    private QueryFileRepository queryFileRepository;

    @BeforeEach
    void setUp() {
        queryFileRepository = mock(QueryFileRepository.class);
        service = new QueryFileServiceImpl(queryFileRepository);
    }

    @Test
    @DisplayName("ID로 파일 조회 성공")
    void findById_success() {
        // given
        Long id = 1L;
        File file = DummyFile.dummy(URL + "/image.png");

        Mockito.when(queryFileRepository.findById(any())).thenReturn(Optional.ofNullable(file));

        // when
        FileResponseDto response = service.findById(id);

        // then
        assertThat(response.getUrl()).isEqualTo(file.getUrl());
    }

    @Test
    @DisplayName("ID로 파일 조회 실패_존재하지 않는 ID로 파일을 조회하려 하는 경우 예외 발생")
    void findById_notFoundId_throwFileNotFoundException() {
        // given
        Long id = 1L;
        Mockito.when(queryFileRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        // when then
        assertThatThrownBy(() -> service.findById(id)).isInstanceOf(FileNotFoundException.class);
    }

}
