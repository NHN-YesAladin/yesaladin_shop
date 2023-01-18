package shop.yesaladin.shop.payment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.payment.domain.repository.QueryPaymentRepository;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.exception.PaymentNotFoundException;
import shop.yesaladin.shop.payment.service.inter.QueryPaymentService;

/**
 * 결제 정보 조회를 위한 서비스 구현체
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class QueryPaymentServiceImpl implements QueryPaymentService {

    private final QueryPaymentRepository queryPaymentRepository;

    @Transactional(readOnly = true)
    @Override
    public PaymentCompleteSimpleResponseDto findByOrderId(Long orderId) {
        return queryPaymentRepository.findSimpleDtoById(null, orderId)
                .orElseThrow(() -> new PaymentNotFoundException(orderId));
    }
}
