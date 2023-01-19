package shop.yesaladin.shop.file.exception;

/**
 * Id or Url에 맞는 파일이 없는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(Long id) {
        super("File ID(" + id + ") is not found.");
    }

    public FileNotFoundException(String url) {
        super("File URL(" + url + ") is not found.");
    }
}
