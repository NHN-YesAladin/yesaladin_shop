package shop.yesaladin.shop.payment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.payment.domain.model.Payment;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Payment findByOrderId(long orderId) {
        //TODO 서비스 테스트 필요
        return queryPaymentRepository.findById(null, orderId).orElseThrow(() -> new ClientException(
                ErrorCode.PAYMENT_NOT_FOUND, ErrorCode.PAYMENT_NOT_FOUND.getDisplayName()));
    }

}
