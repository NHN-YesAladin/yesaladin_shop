package shop.yesaladin.shop.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.yesaladin.shop.file.domain.model.File;

import java.time.LocalDateTime;

/**
 * 파일 데이터를 반환받는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {

    private Long id;
    private String url;
    private LocalDateTime uploadDateTime;

    public File toEntity() {
        return File.builder().id(id).url(url).uploadDateTime(uploadDateTime).build();
    }
}
