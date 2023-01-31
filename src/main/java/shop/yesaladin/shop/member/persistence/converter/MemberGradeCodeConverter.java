package shop.yesaladin.shop.member.persistence.converter;

import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

/**
 * 회원의 등급을 변환하기 위한 컨버터 클래스 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Converter(autoApply = true)
public class MemberGradeCodeConverter implements AttributeConverter<MemberGrade, Integer> {

    /**
     * 회원 등급 enum을 primary key인 Interger로 변환합니다.
     *
     * @param memberGrade 회원 등급
     * @return 회원 등급의 Id
     * @author 송학현
     * @since 1.0
     */
    @Override
    public Integer convertToDatabaseColumn(MemberGrade memberGrade) {
        return memberGrade.getId();
    }


    /**
     * 회원 등급의 pk를 회원 등급으로 변환합니다.
     *
     * @param id 회원 등급의 PK
     * @return 회원 등급
     * @author 송학현
     * @since 1.0
     */
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
