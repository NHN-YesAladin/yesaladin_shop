package shop.yesaladin.shop.file.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.domain.repository.CommandFileRepository;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.product.dummy.DummyFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandFileServiceImplTest {

    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private CommandFileService service;
    private CommandFileRepository commandFileRepository;

    @BeforeEach
    void setUp() {
        commandFileRepository = mock(CommandFileRepository.class);
        service = new CommandFileServiceImpl(commandFileRepository);
    }

    @Test
    @DisplayName("파일 등록 성공")
    void register() {
        // given
        File file = DummyFile.dummy(URL + "/Ex.extension");

        when(commandFileRepository.save(any())).thenReturn(file);

        // when
        FileResponseDto response = service.register(file);

        // then
        assertThat(response.getUrl()).isEqualTo(file.getUrl());

        verify(commandFileRepository, times(1)).save(file);
    }
}
