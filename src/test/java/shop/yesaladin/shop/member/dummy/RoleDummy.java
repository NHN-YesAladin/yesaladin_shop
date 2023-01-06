package shop.yesaladin.shop.member.dummy;

import shop.yesaladin.shop.member.domain.model.Role;

public class RoleDummy {
    public static Role dummy() {
        String ROLE_NAME = "ROLE_MEMBER";

        return Role.builder()
                .name(ROLE_NAME)
                .build();
    }
}
