package shop.yesaladin.shop.product.dummy;

import java.time.LocalDateTime;
import java.util.UUID;
import shop.yesaladin.shop.file.domain.model.File;

public class DummyFile {

    public static File dummy(String extension) {
        return File.builder()
                .fileName("UUID." + extension)
                .uploadDateTime(LocalDateTime.now())
                .build();
    }
}
