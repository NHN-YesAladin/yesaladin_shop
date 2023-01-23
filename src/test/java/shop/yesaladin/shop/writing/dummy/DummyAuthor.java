package shop.yesaladin.shop.writing.dummy;

import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.writing.domain.model.Author;

public class DummyAuthor {

    public static Author dummy(String name, Member member) {
        return Author.builder()
                .name(name)
                .member(member)
                .build();
    }
}
