package shop.yesaladin.shop.member.persistence.converter;

import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;


/**
 * 회원의 성별 코드를 변환하기 위한 컨버터 클래스 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Converter(autoApply = true)
public class MemberGenderCodeConverter implements AttributeConverter<MemberGenderCode, Integer> {

    /**
     * 회원성별코드를 Integer 타입으로 변환합니다.
     *
     * @param memberGenderCode 회원성별코드
     * @return 회원성별코드의 Id
     * @author 송학현
     * @since 1.0
     */
    @Override
    public Integer convertToDatabaseColumn(MemberGenderCode memberGenderCode) {
        return memberGenderCode.getGender();
    }

    /**
     * pk를 회원성별코드로 변환합니다.
     *
     * @param gender 회원성별코드의 Id
     * @return 회원성별코드
     * @author 송학현
     * @since 1.0
     */
    @Override
    public MemberGenderCode convertToEntityAttribute(Integer gender) {
        if (Objects.isNull(gender)) {
            return null;
        }

        return Stream.of(MemberGenderCode.values())
                .filter(g -> gender.equals(g.getGender()))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
