package shop.yesaladin.shop.coupon.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;

@Transactional
@SpringBootTest
class MyBatisInsertMemberCouponMapperTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private MyBatisInsertMemberCouponMapper mapper;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("회원 쿠폰 등록 성공")
    void insertMemberCoupon() {
        // given
        Member member = MemberDummy.dummy();
        em.persist(member);

        List<MemberCouponRequestDto> requestDtoList = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            List<String> couponCodes = new ArrayList<>();
            List<String> couponGroupCodes = new ArrayList<>();

            for (int j = 0; j < 10; j++) {
                couponCodes.add(UUID.randomUUID().toString());
                couponGroupCodes.add(UUID.randomUUID().toString());
            }

            MemberCouponRequestDto requestDto = new MemberCouponRequestDto(
                    member.getId(),
                    couponCodes,
                    couponGroupCodes
            );
            requestDtoList.add(requestDto);
        }

        // when
        int count = mapper.insertMemberCoupon(requestDtoList);

        // then
        Assertions.assertThat(count).isEqualTo(5000);
    }
}