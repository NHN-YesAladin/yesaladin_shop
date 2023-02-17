package shop.yesaladin.shop.coupon.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.coupon.adapter.websocket.CouponResultRedisPublisher;
import shop.yesaladin.shop.coupon.domain.model.CouponSocketRequestKind;
import shop.yesaladin.shop.coupon.domain.repository.CouponGiveResultMessageRepository;
import shop.yesaladin.shop.coupon.domain.repository.CouponGiveSocketConnectionRepository;
import shop.yesaladin.shop.coupon.dto.CouponResultDto;

class CouponWebsocketMessageSendServiceImplTest {

    private CouponResultRedisPublisher publisher;
    private CouponGiveResultMessageRepository messageRepository;
    private CouponGiveSocketConnectionRepository connectionRepository;
    private CouponWebsocketMessageSendServiceImpl service;

    @BeforeEach
    void setUp() {
        publisher = Mockito.mock(CouponResultRedisPublisher.class);
        messageRepository = Mockito.mock(CouponGiveResultMessageRepository.class);
        connectionRepository = Mockito.mock(CouponGiveSocketConnectionRepository.class);
        service = new CouponWebsocketMessageSendServiceImpl(
                publisher,
                messageRepository,
                connectionRepository
        );
    }

    @Test
    @DisplayName("클라이언트가 연결되어있다면 메시지를 발행한다.")
    void sendGiveCouponResultMessageTest() {
        // given
        CouponResultDto message = new CouponResultDto(CouponSocketRequestKind.GIVE, "123", true, "message");
        Mockito.when(connectionRepository.existsByRequestId("123")).thenReturn(true);

        // when
        service.trySendGiveCouponResultMessage(message);

        // then
        Mockito.verify(connectionRepository, Mockito.times(1)).existsByRequestId("123");
        Mockito.verify(messageRepository, Mockito.times(1)).deleteByRequestId("123");
        Mockito.verify(connectionRepository, Mockito.times(1)).delete("123");
        Mockito.verify(publisher, Mockito.times(1)).publish(message);
        Mockito.verify(messageRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("클라이언트가 연결되어있지 않다면 메시지를 저장한다.")
    void saveGiveCouponResultMessageTest() {
        // given
        CouponResultDto message = new CouponResultDto(CouponSocketRequestKind.GIVE, "123", true, "message");
        Mockito.when(connectionRepository.existsByRequestId("123")).thenReturn(false);

        // when
        service.trySendGiveCouponResultMessage(message);

        // then
        Mockito.verify(connectionRepository, Mockito.times(1)).existsByRequestId("123");
        Mockito.verify(messageRepository, Mockito.never()).deleteByRequestId(Mockito.any());
        Mockito.verify(connectionRepository, Mockito.never()).delete(Mockito.any());
        Mockito.verify(publisher, Mockito.never()).publish(Mockito.any());
        Mockito.verify(messageRepository, Mockito.times(1)).save(message);
    }

    @Test
    @DisplayName("클라이언트 연결 정보 등록에 성공한다.")
    void registerConnectionTest() {
        // given
        Mockito.when(messageRepository.existsByRequestId("123")).thenReturn(false);

        // when
        service.registerConnection("123");

        // then
        Mockito.verify(messageRepository, Mockito.times(1)).existsByRequestId("123");
        Mockito.verify(messageRepository, Mockito.never()).deleteByRequestId(Mockito.any());
        Mockito.verify(connectionRepository, Mockito.never()).delete(Mockito.any());
        Mockito.verify(publisher, Mockito.never()).publish(Mockito.any());
        Mockito.verify(connectionRepository, Mockito.times(1)).save("123");
    }

    @Test
    @DisplayName("발행된 메시지가 존재한다면 메시지를 발행한다.")
    void registerConnectionAndSendGiveCouponResultMessageTest() {
        // given
        CouponResultDto message = new CouponResultDto(CouponSocketRequestKind.GIVE, "123", true, "message");
        Mockito.when(messageRepository.existsByRequestId("123")).thenReturn(true);
        Mockito.when(connectionRepository.existsByRequestId("123")).thenReturn(true);
        Mockito.when(messageRepository.getByRequestId("123")).thenReturn(message);

        // when
        service.registerConnection("123");

        // then
        Mockito.verify(connectionRepository, Mockito.times(1)).existsByRequestId("123");
        Mockito.verify(messageRepository, Mockito.times(1)).deleteByRequestId("123");
        Mockito.verify(connectionRepository, Mockito.times(1)).delete("123");
        Mockito.verify(publisher, Mockito.times(1)).publish(message);
        Mockito.verify(messageRepository, Mockito.never()).save(Mockito.any());
    }
}