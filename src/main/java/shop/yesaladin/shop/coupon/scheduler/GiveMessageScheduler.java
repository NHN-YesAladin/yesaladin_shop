package shop.yesaladin.shop.coupon.scheduler;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.yesaladin.shop.coupon.domain.model.CouponGiveRequestResponseMessage;
import shop.yesaladin.shop.coupon.domain.model.ProcessingStatus;
import shop.yesaladin.shop.coupon.event.CouponGiveResponseEvent;

@RequiredArgsConstructor
@Component
public class GiveMessageScheduler {

    private final MongoTemplate mongoTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Scheduled(fixedDelay = 500)
    public void consumeGiveRequestResponseMessage() {
        Query query = new Query(Criteria.where("processed")
                .is(ProcessingStatus.NOT_PROCESSED)).limit(1);
        CouponGiveRequestResponseMessage message = mongoTemplate.findOne(
                query,
                CouponGiveRequestResponseMessage.class
        );

        if (Objects.nonNull(message)) {
            startProcess(message);
            mongoTemplate.update(message.getClass());
            eventPublisher.publishEvent(new CouponGiveResponseEvent(this, message));
        }
    }

    @Scheduled(fixedDelay = 30000)
    public void markAsFail() {
        Query query = new Query(Criteria.where("processed")
                .is(ProcessingStatus.PROCESSING)
                .and("updatedDateTime")
                .lt(LocalDateTime.now(clock).minusSeconds(30)));
        Update update = new Update();
        update.set("processed", ProcessingStatus.FAILED);
        update.set("updatedDateTime", LocalDateTime.now(clock));
        mongoTemplate.updateMulti(query, update, CouponGiveRequestResponseMessage.class);
    }

    private void startProcess(CouponGiveRequestResponseMessage message) {
        Query query = new Query(Criteria.where("_id").is(message.getRequestId()));
        Update update = new Update();
        update.set("processed", ProcessingStatus.PROCESSING);
        update.set("updatedDateTime", LocalDateTime.now(clock));
        mongoTemplate.updateFirst(query, update, message.getClass());
    }
}
