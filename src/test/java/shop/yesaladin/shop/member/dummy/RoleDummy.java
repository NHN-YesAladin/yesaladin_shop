package shop.yesaladin.shop.member.dummy;

import shop.yesaladin.shop.member.domain.model.Role;

public class RoleDummy {

    public static Role dummy() {
        String roleName = "ROLE_MEMBER";

        return Role.builder()
                .name(roleName)
                .build();
    }

    public static Role dummyWithId() {
        int roleId = 1;
        String roleName = "ROLE_MEMBER";

        return Role.builder()
                .id(roleId)
                .name(roleName)
                .build();
    }
}
