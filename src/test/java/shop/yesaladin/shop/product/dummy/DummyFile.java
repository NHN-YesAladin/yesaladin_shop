package shop.yesaladin.shop.product.dummy;

import java.time.LocalDateTime;
import java.util.UUID;
import shop.yesaladin.shop.file.domain.model.File;

public class DummyFile {
    public static File dummy() {
        return File.builder()
                .uuid(UUID.randomUUID().toString())
                .extension(".jpg")
                .uploadDateTime(LocalDateTime.now())
                .build();
    }
}
