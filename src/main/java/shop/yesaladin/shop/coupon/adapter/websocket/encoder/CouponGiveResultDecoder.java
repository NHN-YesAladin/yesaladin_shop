package shop.yesaladin.shop.coupon.adapter.websocket.encoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.yesaladin.shop.coupon.dto.CouponGiveResultDto;

@RequiredArgsConstructor
@Component
public class CouponGiveResultDecoder implements Decoder.Text<CouponGiveResultDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CouponGiveResultDto decode(String s) throws DecodeException {
        try {
            return objectMapper.readValue(s, CouponGiveResultDto.class);
        } catch (JsonProcessingException e) {
            throw new DecodeException(s, "message decode fail", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return Objects.nonNull(s);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
