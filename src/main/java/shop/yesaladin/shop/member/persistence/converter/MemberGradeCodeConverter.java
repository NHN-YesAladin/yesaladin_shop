package shop.yesaladin.shop.member.persistence.converter;

import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

@Converter(autoApply = true)
public class MemberGradeCodeConverter implements AttributeConverter<MemberGrade, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MemberGrade memberGrade) {
        return memberGrade.getId();
    }

    @Override
    public MemberGrade convertToEntityAttribute(Integer id) {
        if (Objects.isNull(id)) {
            return null;
        }

        return Stream.of(MemberGrade.values())
                .filter(g -> id.equals(g.getId()))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
