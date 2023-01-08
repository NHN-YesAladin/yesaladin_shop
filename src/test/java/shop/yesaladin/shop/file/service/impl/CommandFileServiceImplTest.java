package shop.yesaladin.shop.file.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.domain.repository.CommandFileRepository;
import shop.yesaladin.shop.file.service.inter.CommandFileService;

class CommandFileServiceImplTest {

    private final String FILE_NAME = "UUID.png";

    private CommandFileService commandFileService;
    private CommandFileRepository commandFileRepository;

    @BeforeEach
    void setUp() {
        commandFileRepository = mock(CommandFileRepository.class);
        commandFileService = new CommandFileServiceImpl(commandFileRepository);
    }

    @Test
    void register() {
        // given
        LocalDateTime now = LocalDateTime.now();

        File file = File.builder()
                .fileName(FILE_NAME)
                .uploadDateTime(now)
                .build();

        when(commandFileRepository.save(any())).thenReturn(file);

        // when
        File registeredFile = commandFileService.register(file);

        // then
        assertThat(registeredFile.getFileName()).isEqualTo(FILE_NAME);
        assertThat(registeredFile.getUploadDateTime()).isEqualTo(now);
    }
}
