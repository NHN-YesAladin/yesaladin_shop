package shop.yesaladin.shop.coupon.adapter.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.shop.coupon.adapter.websocket.encoder.CouponGiveResultDecoder;
import shop.yesaladin.shop.coupon.adapter.websocket.encoder.CouponGiveResultEncoder;
import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;

@Slf4j
@Component
@ServerEndpoint(value = "/coupon/give/result/{requestId}", encoders = CouponGiveResultEncoder.class, decoders = CouponGiveResultDecoder.class)
public class CouponGiveResultHandler {

    private Session session;
    private final static Map<CouponGiveResultHandler, String> handlerMap = new ConcurrentHashMap<>();
    private final static Map<String, CouponGiveResultHandler> requestIdMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("requestId") String requestId) {
        log.info("Request id {} socket open", requestId);
        this.session = session;
        requestIdMap.put(requestId, this);
        handlerMap.put(this, requestId);
    }

    @OnClose
    public void onClose(Session session) {
        String requestId = handlerMap.remove(this);
        requestIdMap.remove(requestId);
        log.info("Request id {} socket close", requestId);
    }

    public static void sendResultToClient(CouponGiveResultDto resultDto) {
        String requestId = resultDto.getRequestId();
        CouponGiveResultHandler handler = requestIdMap.get(resultDto.getRequestId());
        try {
            handler.session.getBasicRemote().sendObject(resultDto);
        } catch (IOException | EncodeException e) {
            throw new ServerException(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    "Fail to sending coupon give result. Request id : " + requestId
            );
        }
    }

}
