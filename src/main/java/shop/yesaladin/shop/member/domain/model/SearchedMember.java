package shop.yesaladin.shop.member.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 엘라스틱서치에서 검색된 회원의 정보 document 입니다.
 *
 * @author : 김선홍
 * @since : 1.0
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "members")
public class SearchedMember {
    @Id
    @Field(name = "id", type = FieldType.Long)
    Long id;
    @Field(name = "nickname", type = FieldType.Keyword)
    String nickname;
    @Field(name = "name", type = FieldType.Text, analyzer = "custom_analyzer")
    private String name;
    @Field(name = "login_id", type = FieldType.Keyword)
    private String loginId;

    @Field(name = "phone", type = FieldType.Keyword)
    private String phone;
    @Field(name = "birth_year", type = FieldType.Integer)
    private int birthYear;
    @Field(name = "birth_month", type = FieldType.Integer)
    private int birthMonth;
    @Field(name = "birth_day", type = FieldType.Integer)
    private int birthDay;
    @Field(name = "email", type = FieldType.Keyword)
    private String email;
    @Field(name = "sign_up_date", type = FieldType.Date)
    private LocalDate signUpDate;
    @Field(name = "withdrawal_date", type = FieldType.Date)
    private LocalDate withdrawalDate;
    @Field(name = "is_withdrawal", type = FieldType.Boolean)
    private boolean isWithdrawal;
    @Field(name = "is_blocked", type = FieldType.Boolean)
    private boolean isBlocked;
    @Field(name = "point", type = FieldType.Long)
    private Long point;
    @Field(name = "grade", type = FieldType.Keyword)
    private String grade;
    @JsonFormat(shape = Shape.STRING)
    private MemberGenderCode gender;
}
