package shop.yesaladin.shop.coupon.adapter.websocket.encoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import lombok.RequiredArgsConstructor;
import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;

@RequiredArgsConstructor
public class CouponGiveResultEncoder implements Encoder.Text<CouponGiveResultDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(CouponGiveResultDto couponGiveResultDto) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(couponGiveResultDto);
        } catch (JsonProcessingException e) {
            throw new EncodeException(couponGiveResultDto, "message encode fail", e);
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
