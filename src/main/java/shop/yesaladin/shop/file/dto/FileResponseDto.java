package shop.yesaladin.shop.file.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.yesaladin.shop.file.domain.model.File;

/**
 * 파일 데이터를 반환받는 DTO 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileResponseDto {
    private Long id;
    private String name;
    private LocalDateTime uploadDateTime;

    public File toEntity() {
        return File.builder().id(id).name(name).uploadDateTime(uploadDateTime).build();
    }
}
