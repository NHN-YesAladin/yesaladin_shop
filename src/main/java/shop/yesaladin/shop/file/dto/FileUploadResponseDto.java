package shop.yesaladin.shop.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 파일 업로드 후 정보를 전달하기 위한 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponseDto {

    private String url;
    private String fileUploadDateTime;
}
