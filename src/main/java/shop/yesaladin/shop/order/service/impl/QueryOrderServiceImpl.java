package shop.yesaladin.shop.order.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.PageOffsetOutOfBoundsException;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.dto.MemberOrderSheetResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.repository.QueryOrderProductRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderStatusChangeLogRepository;
import shop.yesaladin.shop.order.dto.OrderDetailsResponseDto;
import shop.yesaladin.shop.order.dto.OrderPaymentResponseDto;
import shop.yesaladin.shop.order.dto.OrderResponseDto;
import shop.yesaladin.shop.order.dto.OrderSheetRequestDto;
import shop.yesaladin.shop.order.dto.OrderSheetResponseDto;
import shop.yesaladin.shop.order.dto.OrderStatusResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.order.exception.OrderNotFoundException;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;
import shop.yesaladin.shop.payment.dto.PaymentResponseDto;
import shop.yesaladin.shop.payment.service.inter.QueryPaymentService;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;
import shop.yesaladin.shop.product.dto.ProductOrderSheetResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

/**
 * 주문 데이터 조회 서비스의 구현체
 *
 * @author 김홍대
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryOrderServiceImpl implements QueryOrderService {

    private final QueryOrderRepository queryOrderRepository;
    private final QueryOrderProductRepository queryOrderProductRepository;
    private final QueryMemberService queryMemberService;
    private final QueryMemberAddressService queryMemberAddressService;
    private final QueryPointHistoryService queryPointHistoryService;
    private final QueryProductService queryProductService;
    private final QueryMemberCouponService queryMemberCouponService;
    private final QueryPaymentService queryPaymentService;
    private final QueryOrderStatusChangeLogRepository queryOrderStatusChangeLogRepository;

    private final Clock clock;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryDto> getAllOrderListInPeriod(
            PeriodQueryRequestDto queryDto, Pageable pageable
    ) {
        queryDto.validate(clock);

        LocalDate startDate = queryDto.getStartDateOrDefaultValue(clock);
        LocalDate endDate = queryDto.getEndDateOrDefaultValue(clock);

        checkRequestedOffsetInBounds(startDate, endDate, null, pageable);
        return queryOrderRepository.findAllOrdersInPeriod(startDate, endDate, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryDto> getAllOrderListInPeriodByMemberId(
            PeriodQueryRequestDto queryDto, long memberId, Pageable pageable
    ) {
        checkValidMemberId(memberId);
        queryDto.validate(clock);

        LocalDate startDate = queryDto.getStartDateOrDefaultValue(clock);
        LocalDate endDate = queryDto.getEndDateOrDefaultValue(clock);

        checkRequestedOffsetInBounds(startDate, endDate, memberId, pageable);
        return queryOrderRepository.findAllOrdersInPeriodByMemberId(startDate,
                endDate,
                memberId,
                pageable
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Order getOrderByNumber(String number) {
        return tryGetOrder(number);
    }

    private Order tryGetOrder(String number) {
        return queryOrderRepository.findByOrderNumber(number)
                .orElseThrow(() -> new OrderNotFoundException(number));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OrderSheetResponseDto getNonMemberOrderSheetData(OrderSheetRequestDto request) {
        List<ProductOrderSheetResponseDto> orderProducts = getProductOrder(request.getIsbn(),
                request.getQuantity()
        );
        return new OrderSheetResponseDto(orderProducts);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OrderSheetResponseDto getMemberOrderSheetData(
            OrderSheetRequestDto request, String loginId
    ) {
        checkValidLoginId(loginId);

        return getOrderSheetDataForMember(request, loginId);
    }

    private OrderSheetResponseDto getOrderSheetDataForMember(
            OrderSheetRequestDto request, String loginId
    ) {
        MemberOrderSheetResponseDto member = queryMemberService.getMemberForOrder(loginId);
        List<MemberAddressResponseDto> memberAddress = queryMemberAddressService.getByLoginId(
                loginId);
        List<ProductOrderSheetResponseDto> orderProducts = getProductOrder(request.getIsbn(),
                request.getQuantity()
        );

        List<MemberCouponSummaryDto> memberCoupons = getMemberCoupons(loginId,
                member.getCouponCount()
        );

        return new OrderSheetResponseDto(member,
                queryPointHistoryService.getMemberPoint(loginId),
                orderProducts,
                memberAddress,
                memberCoupons
        );
    }

    private List<ProductOrderSheetResponseDto> getProductOrder(
            List<String> isbnList, List<Integer> quantityList
    ) {
        Map<String, Integer> products = IntStream.range(0, isbnList.size())
                .boxed()
                .collect(Collectors.toMap(isbnList::get, quantityList::get));

        return queryProductService.getByOrderProducts(products);
    }

    private List<MemberCouponSummaryDto> getMemberCoupons(
            String loginId, int totalPage
    ) {
        int offset = 20;
        List<MemberCouponSummaryDto> memberCoupons = new ArrayList<>();

        PaginatedResponseDto<MemberCouponSummaryDto> coupons;
        for (int i = 0; i < totalPage; i++) {
            coupons = queryMemberCouponService.getMemberCouponSummaryList(PageRequest.of(i, offset),
                    loginId,
                    true
            );
            memberCoupons.addAll(coupons.getDataList());
        }
        return memberCoupons;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponseDto> getOrderListInPeriodByMemberId(
            PeriodQueryRequestDto queryDto, String loginId, Pageable pageable
    ) {
        Member foundMember = queryMemberService.findByLoginId(loginId);
        queryDto.validate(clock);

        LocalDate startDate = queryDto.getStartDateOrDefaultValue(clock);
        LocalDate endDate = queryDto.getEndDateOrDefaultValue(clock);

        checkRequestedOffsetInBounds(startDate, endDate, foundMember.getId(), pageable);
        return queryOrderRepository.findOrdersInPeriodByMemberId(startDate,
                endDate,
                foundMember.getId(),
                pageable
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OrderPaymentResponseDto getPaymentDtoByMemberOrderId(long orderId) {
        return queryOrderRepository.findPaymentDtoByMemberOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderStatusResponseDto> getStatusResponsesByLoginIdAndStatus(
            String loginId, OrderStatusCode code, Pageable pageable
    ) {
        checkValidLoginId(loginId);
        return queryOrderRepository.findSuccessStatusResponsesByLoginIdAndStatus(loginId,
                code,
                pageable
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Map<OrderStatusCode, Long> getOrderCountByLoginIdStatus(String loginId) {
        checkValidLoginId(loginId);

        Map<OrderStatusCode, Long> map = new HashMap<>();
        for (OrderStatusCode code : OrderStatusCode.values()) {
            if (code.equals(OrderStatusCode.DEPOSIT) || code.equals(OrderStatusCode.REFUND)
                    || code.equals(OrderStatusCode.CANCEL)) {
                continue;
            }
            long count = queryOrderRepository.getOrderCountByStatusCode(loginId, code);
            map.put(code, count);
        }
        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDetailsResponseDto getDetailsDtoByOrderNumber(String orderNumber) {
        OrderDetailsResponseDto responseDto = new OrderDetailsResponseDto();

        //주문 정보 set : findByOrderNumber로 order 불러와서 code로 회원 , 비회원 캐스팅
        Order order = tryGetOrder(orderNumber);
        setOrderToResponseByOrderCode(responseDto, order);

        //상품 정보 set : 주문 상품 레포에서 가져오기
        responseDto.setOrderProducts(queryOrderProductRepository.findAllByOrderNumber(orderNumber));

        //결제 정보 set : 결제 method 보고 맞춰서 set하기
        try {
            setPaymentToResponseByOrderId(responseDto, order);
        } catch (ClientException e) {
            if (!e.getErrorCode().equals(ErrorCode.PAYMENT_NOT_FOUND)) {
                throw new ClientException(ErrorCode.BAD_REQUEST,
                        ErrorCode.BAD_REQUEST.getDisplayName()
                );
            }
            //ignore
            responseDto.setPayment(null);
        }

        //가격 정보 set
        responseDto.calculateAmounts();

        return responseDto;
    }

    private void setPaymentToResponseByOrderId(OrderDetailsResponseDto responseDto, Order order) {
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        Payment payment = queryPaymentService.findByOrderId(order.getId());
        if (payment.getMethod().equals(PaymentCode.EASY_PAY)) {
            paymentResponseDto.setEasyPayInfo(payment);
        } else {
            paymentResponseDto.setCardPayInfo(payment);
        }
        responseDto.setPayment(paymentResponseDto);
    }

    private void setOrderToResponseByOrderCode(OrderDetailsResponseDto responseDto, Order order) {
        OrderCode orderCode = order.getOrderCode();
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        if (orderCode.equals(OrderCode.NON_MEMBER_ORDER)) {
            NonMemberOrder nonMemberOrder = (NonMemberOrder) order;
            orderResponseDto.setOrderInfoFromNonMemberOrder(nonMemberOrder);
        } else if (orderCode.equals(OrderCode.MEMBER_ORDER)) {
            MemberOrder memberOrder = (MemberOrder) order;
            orderResponseDto.setOrderInfoFromMemberOrder(memberOrder);
        } else {
            Subscribe subscribe = (Subscribe) order;
            orderResponseDto.setOrderInfoFromSubscribe(subscribe);
        }
        OrderStatusChangeLog latestChangeLog = queryOrderStatusChangeLogRepository.findFirstByOrder_IdOrderByOrderStatusCodeDesc(
                        order.getId())
                .orElseThrow(() -> new ClientException(ErrorCode.NOT_FOUND,
                        "주문 상태 이력을 찾을 수 없습니다."));
        orderResponseDto.setOrderStatusCode(latestChangeLog.getOrderStatusCode());
        responseDto.setOrder(orderResponseDto);
    }

    private void checkRequestedOffsetInBounds(
            LocalDate startDate, LocalDate endDate, Long memberId, Pageable pageable
    ) {
        long countOfOrder = 0;

        if (Objects.isNull(memberId)) {
            countOfOrder = queryOrderRepository.getCountOfOrdersInPeriod(startDate, endDate);
        } else {
            countOfOrder = queryOrderRepository.getCountOfOrdersInPeriodByMemberId(startDate,
                    endDate,
                    memberId
            );
        }

        if (countOfOrder < pageable.getOffset()) {
            throw new PageOffsetOutOfBoundsException((int) pageable.getOffset(), countOfOrder);
        }
    }

    private void checkValidLoginId(String loginId) {
        if (!queryMemberService.existsLoginId(loginId)) {
            throw new ClientException(ErrorCode.MEMBER_NOT_FOUND,
                    "Member not found with loginId : " + loginId
            );
        }
    }

    private void checkValidMemberId(long memberId) {
        queryMemberService.findMemberById(memberId);
    }
}
