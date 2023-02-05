package shop.yesaladin.shop.product.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.exception.ProductTypeCodeNotFoundException;

/**
 * Enum 클래스로 만들어진 ProductTypeCode 테이블을 entity와 DB 사이의 변환하기 위한 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Converter
public class ProductTypeCodeConverter implements AttributeConverter<ProductTypeCode, Integer> {

    /**
     * 주어진 enum 상수를 DB에 어떤 값으로 넣을 것인지 찾아 리턴합니다.
     *
     * @param productTypeCode enum 상수
     * @return enum 상수에 따른 id
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Integer convertToDatabaseColumn(ProductTypeCode productTypeCode) {
        return productTypeCode.getId();
    }

    /**
     * DB에서 읽힌 id에 따라 어떤 enum이랑 매칭시킬것인지 찾아 리턴합니다.
     *
     * @param id DB에서 읽힌 id
     * @return id에 따라 매치되는 enum 상수
     * @author 이수정
     * @since 1.0
     */
    @Override
    public ProductTypeCode convertToEntityAttribute(Integer id) {
        return Arrays.stream(ProductTypeCode.values())
                .filter(code -> id.equals(code.getId()))
                .findAny()
                .orElseThrow(() -> new ProductTypeCodeNotFoundException(id));
    }
}
