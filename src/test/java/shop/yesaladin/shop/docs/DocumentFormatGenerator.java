package shop.yesaladin.shop.docs;


import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public interface DocumentFormatGenerator {

    static Attributes.Attribute getDateFormat() {
        return key("format").value("yyyy-MM-dd");
    }

    static Attributes.Attribute defaultValue(Object defaultValue) {
        return key("default value").value(defaultValue);
    }
}
