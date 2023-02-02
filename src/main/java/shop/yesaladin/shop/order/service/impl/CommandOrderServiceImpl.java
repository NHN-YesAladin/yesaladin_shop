package shop.yesaladin.shop.order.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.common.utils.AuthorityUtils;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.repository.CommandOrderProductRepository;
import shop.yesaladin.shop.order.domain.repository.CommandOrderRepository;
import shop.yesaladin.shop.order.domain.repository.CommandOrderStatusChangeLogRepository;
import shop.yesaladin.shop.order.dto.OrderCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderCreateResponseDto;
import shop.yesaladin.shop.order.exception.BadRequestException;
import shop.yesaladin.shop.order.service.inter.CommandOrderService;
import shop.yesaladin.shop.point.domain.model.PointReasonCode;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

/**
 * 주문 생성/수정/삭제와 관련한 서비스 구현체 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandOrderServiceImpl implements CommandOrderService {

    private final CommandOrderRepository<NonMemberOrder> nonMemberOrderCommandOrderRepository;
    private final CommandOrderRepository<MemberOrder> memberOrderCommandOrderRepository;
    private final CommandOrderRepository<Subscribe> subscribeCommandOrderRepository;

    private final CommandOrderStatusChangeLogRepository commandOrderStatusChangeLogRepository;
    private final CommandOrderProductRepository commandOrderProductRepository;
    private final CommandPointHistoryService commandPointHistoryService;
    private final QueryMemberAddressService queryMemberAddressService;
    private final QueryProductService queryProductService;
    private final QueryMemberService queryMemberService;

    private final Clock clock;


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OrderCreateResponseDto createOrderWith(
            OrderCode orderCode,
            OrderCreateRequestDto request,
            UserDetails userDetails
    ) {
        LocalDateTime orderDateTime = LocalDateTime.now(clock);

        Order savedOrder = (AuthorityUtils.isAuthorized(userDetails)) ?
                createMemberOrders(orderCode, request, userDetails, orderDateTime)
                : createNonMemberOrders(request, orderDateTime);

        createOrderStatusChangeLog(orderDateTime, savedOrder);

        return OrderCreateResponseDto.fromEntity(savedOrder);
    }

    private Order createNonMemberOrders(
            OrderCreateRequestDto request,
            LocalDateTime orderDateTime
    ) {
        checkValidationForNonMemberOrder(request);

        Map<String, Product> products = queryProductService.findByIsbnList(request.getOrderProducts());

        String name = generateOrderName(List.copyOf(products.values()));
        String number = generateOrderNumber(orderDateTime);

        return createNonMemberOrder(request, orderDateTime, products, name, number);
    }

    private Order createMemberOrders(
            OrderCode orderCode,
            OrderCreateRequestDto request,
            UserDetails userDetails,
            LocalDateTime orderDateTime
    ) {
        checkValidationForMemberOrders(request);

        String loginId = userDetails.getUsername();
        Map<String, Product> products = queryProductService.findByIsbnList(request.getOrderProducts());

        String name = generateOrderName(List.copyOf(products.values()));
        String number = generateOrderNumber(orderDateTime);
        Member member = queryMemberService.findMemberByLoginId(loginId).toEntity();
        MemberAddress address = queryMemberAddressService.findById(request.getOrdererAddressId());

        Order order = (orderCode.equals(OrderCode.MEMBER_SUBSCRIBE)) ?
                creatSubscribe(request, orderDateTime, name, number, member, address) :
                createMemberOrder(request, orderDateTime, products, name, number, member, address);

        createUsePointHistory(request, loginId);

        return order;
    }

    private Order createNonMemberOrder(
            OrderCreateRequestDto request,
            LocalDateTime orderDateTime,
            Map<String, Product> products,
            String name,
            String number
    ) {
        NonMemberOrder nonMemberOrder = request.toEntity(name, number, orderDateTime);
        Order savedOrder = nonMemberOrderCommandOrderRepository.save(nonMemberOrder);

        createOrderProduct(request, products, nonMemberOrder);

        return savedOrder;
    }

    private Order createMemberOrder(
            OrderCreateRequestDto request,
            LocalDateTime orderDateTime,
            Map<String, Product> products,
            String name,
            String orderNumber,
            Member member,
            MemberAddress memberAddress
    ) {
        checkValidationForMemberOrder(request);

        MemberOrder memberOrder = request.toEntity(
                name,
                orderNumber,
                orderDateTime,
                member,
                memberAddress
        );
        Order savedOrder = memberOrderCommandOrderRepository.save(memberOrder);

        createOrderProduct(request, products, memberOrder);

        return savedOrder;
    }

    private Order creatSubscribe(
            OrderCreateRequestDto request,
            LocalDateTime orderDateTime,
            String name,
            String orderNumber,
            Member member,
            MemberAddress memberAddress
    ) {
        LocalDate nextRenewalDate = orderDateTime.withDayOfMonth(request.getExpectedDay())
                .toLocalDate();
        if (nextRenewalDate.isAfter(ChronoLocalDate.from(orderDateTime))) {
            nextRenewalDate = nextRenewalDate.plusMonths(1);
        }
        String isbn = request.getOrderProducts().get(0).getIsbn();
        SubscribeProduct subscribeProduct = queryProductService.findIssnByIsbn(isbn);

        Subscribe subscribe = request.toEntity(
                name,
                orderNumber,
                orderDateTime,
                member,
                memberAddress,
                nextRenewalDate,
                subscribeProduct
        );
        return subscribeCommandOrderRepository.save(subscribe);
    }

    private void createOrderProduct(
            OrderCreateRequestDto request,
            Map<String, Product> products,
            Order order
    ) {
        request.getOrderProducts()
                .stream()
                .map(orderProduct -> OrderProduct.builder()
                        .quantity(orderProduct.getQuantity())
                        .product(products.get(orderProduct.getIsbn()))
                        .order(order)
                        .build())
                .forEach(commandOrderProductRepository::save);
    }

    private void createUsePointHistory(OrderCreateRequestDto request, String loginId) {
        commandPointHistoryService.use(new PointHistoryRequestDto(
                loginId,
                request.getOrderPoint(),
                PointReasonCode.USE_ORDER
        ));
    }

    private void createOrderStatusChangeLog(LocalDateTime orderDateTime, Order savedOrder) {
        OrderStatusChangeLog orderStatusChangeLog = OrderStatusChangeLog.create(
                savedOrder,
                orderDateTime,
                OrderStatusCode.ORDER
        );
        commandOrderStatusChangeLogRepository.save(orderStatusChangeLog);
    }

    private String generateOrderName(List<Product> products) {
        if (products.size() == 1) {
            return products.get(0).getTitle();
        }
        return products.get(0).getTitle() + "외 " + (products.size() - 1) + "권";
    }

    private String generateOrderNumber(LocalDateTime orderDateTime) {
        return orderDateTime.toLocalDate() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private void checkValidationForNonMemberOrder(OrderCreateRequestDto request) {
        if (hasNonRequiredInfoForNonMemberOrder(request)) {
            throw new BadRequestException();
        }
    }

    private void checkValidationForMemberOrders(OrderCreateRequestDto request) {
        if (hasNonRequiredInfoForMemberOrders(request)) {
            throw new BadRequestException();
        }
    }

    private void checkValidationForMemberOrder(OrderCreateRequestDto request) {
        if (hasNonRequiredInfoForMemberOrder(request)) {
            throw new BadRequestException();
        }
    }

    private boolean hasNonRequiredInfoForNonMemberOrder(OrderCreateRequestDto request) {
        return Objects.nonNull(request.getOrdererAddressId())
                || Objects.nonNull(request.getOrderPoint())
                || Objects.nonNull(request.getOrderCoupons())
                || Objects.nonNull(request.getExpectedDay())
                || Objects.nonNull(request.getIntervalMonth());
    }

    private boolean hasNonRequiredInfoForMemberOrders(OrderCreateRequestDto request) {
        return Objects.isNull(request.getOrdererAddressId());
    }

    private boolean hasNonRequiredInfoForMemberOrder(OrderCreateRequestDto request) {
        return Objects.nonNull(request.getExpectedDay())
                || Objects.nonNull(request.getIntervalMonth());
    }
}
