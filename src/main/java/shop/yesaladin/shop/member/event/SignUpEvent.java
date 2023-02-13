package shop.yesaladin.shop.member.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SignUpEvent extends ApplicationEvent {

    private final String memberId;

    public SignUpEvent(Object source, String memberId) {
        super(source);
        this.memberId = memberId;
    }
}
