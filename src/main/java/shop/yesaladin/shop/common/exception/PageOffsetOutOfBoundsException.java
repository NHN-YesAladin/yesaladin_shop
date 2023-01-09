package shop.yesaladin.shop.common.exception;

/**
 * pagination시 데이터의 총 개수보다 큰 오프셋으로 조회하는 경우 발생하는 예외
 *
 * @author 김홍대
 * @since 1.0
 */
public class PageOffsetOutOfBoundsException extends RuntimeException {

    /**
     * 에러 메시지와 함께 객체를 생성합니다.
     *
     * @param requestedOffset 클라이언트가 요청한 오프셋
     * @param totalDataCount  실제 총 데이터 수
     */
    public PageOffsetOutOfBoundsException(int requestedOffset, long totalDataCount) {
        super("[PageOffSetOutOfBounds] requested offset: " + requestedOffset + ", total data: "
                + totalDataCount);
    }
}
