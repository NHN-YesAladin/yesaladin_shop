package shop.yesaladin.shop.file.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 파일의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "files")
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false, length = 180)
    private String url;

    @LastModifiedDate
    @Column(name = "upload_datetime", nullable = false)
    private LocalDateTime uploadDateTime;

    public void updateFileUrl(String newUrl) {
        this.url = newUrl;
    }
}
