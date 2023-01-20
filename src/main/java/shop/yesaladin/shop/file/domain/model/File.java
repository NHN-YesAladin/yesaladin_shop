package shop.yesaladin.shop.file.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "upload_datetime", nullable = false)
    private LocalDateTime uploadDateTime;

}
