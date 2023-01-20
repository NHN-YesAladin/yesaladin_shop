package shop.yesaladin.shop.product.dummy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import shop.yesaladin.shop.file.domain.model.File;

public class DummyFile {

    private static Clock fileClock = Clock.fixed(
            Instant.parse("2023-01-20T00:00:00.000Z"),
            ZoneId.of("UTC")
    );
    // TODO: 삭제 예정입니다!!
    public static File dummy(String url, LocalDateTime uploadDateTime) {
        return File.builder()
                .url(url)
                .uploadDateTime(uploadDateTime)
                .build();
    }


    public static File dummy(String url) {
        return File.builder()
                .url(url)
                .uploadDateTime(LocalDateTime.now(fileClock))
                .build();
    }
}
