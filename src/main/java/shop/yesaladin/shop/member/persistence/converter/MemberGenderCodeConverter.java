package shop.yesaladin.shop.member.persistence.converter;

import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;


/**
 * 회원의 성별 코드를 변환하여 Database에 입력하는 클래스입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Converter(autoApply = true)
public class MemberGenderCodeConverter implements AttributeConverter<MemberGenderCode, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MemberGenderCode memberGenderCode) {
        if (Objects.isNull(memberGenderCode)) {
            return null;
        }

        return memberGenderCode.getGender();
    }

    @Override
    public MemberGenderCode convertToEntityAttribute(Integer gender) {
        if (Objects.isNull(gender)) {
            return null;
        }

        return Stream.of(MemberGenderCode.values())
                .filter(g -> g.getGender().equals(gender))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
