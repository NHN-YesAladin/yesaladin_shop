package shop.yesaladin.shop.order.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.repository.CommandOrderProductRepository;
import shop.yesaladin.shop.order.domain.repository.CommandOrderRepository;
import shop.yesaladin.shop.order.domain.repository.CommandOrderStatusChangeLogRepository;
import shop.yesaladin.shop.order.dto.OrderCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderCreateResponseDto;
import shop.yesaladin.shop.order.dto.OrderMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderNonMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderSubscribeCreateRequestDto;
import shop.yesaladin.shop.order.service.inter.CommandOrderService;
import shop.yesaladin.shop.point.domain.model.PointReasonCode;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
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
    private final CommandProductService commandProductService;
    private final QueryMemberAddressService queryMemberAddressService;
    private final QueryProductService queryProductService;
    private final QueryMemberService queryMemberService;

    private final Clock clock;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OrderCreateResponseDto createNonMemberOrders(
            OrderNonMemberCreateRequestDto request
    ) {
        LocalDateTime orderDateTime = LocalDateTime.now(clock);
        Map<String, Product> products = commandProductService.orderProducts(request.getOrderProducts());

        Order savedOrder = createNonMemberOrder(request, products, orderDateTime);

        createOrderProduct(request, products, savedOrder);
        createOrderStatusChangeLog(orderDateTime, savedOrder);

        return OrderCreateResponseDto.fromEntity(savedOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OrderCreateResponseDto createMemberOrders(
            OrderMemberCreateRequestDto request,
            String loginId
    ) {
        LocalDateTime orderDateTime = LocalDateTime.now(clock);
        Map<String, Product> products = commandProductService.orderProducts(request.getOrderProducts());

        Order savedOrder = createMemberOrder(request, orderDateTime, products, loginId);

        createOrderProduct(request, products, savedOrder);
        createUsePointHistory(request.getOrderPoint(), loginId);
        createOrderStatusChangeLog(orderDateTime, savedOrder);

        return OrderCreateResponseDto.fromEntity(savedOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OrderCreateResponseDto createSubscribeOrders(
            OrderSubscribeCreateRequestDto request,
            String loginId
    ) {
        LocalDateTime orderDateTime = LocalDateTime.now(clock);

        Order savedOrder = creatSubscribe(request, orderDateTime, loginId);

        createUsePointHistory(request.getOrderPoint(), loginId);
        createOrderStatusChangeLog(orderDateTime, savedOrder);

        return OrderCreateResponseDto.fromEntity(savedOrder);
    }


    private Order createNonMemberOrder(
            OrderNonMemberCreateRequestDto request,
            Map<String, Product> products,
            LocalDateTime orderDateTime
    ) {
        NonMemberOrder nonMemberOrder = request.toEntity(
                generateOrderName(List.copyOf(products.values())),
                generateOrderNumber(orderDateTime),
                orderDateTime
        );

        return nonMemberOrderCommandOrderRepository.save(nonMemberOrder);
    }

    private Order createMemberOrder(
            OrderMemberCreateRequestDto request,
            LocalDateTime orderDateTime,
            Map<String, Product> products,
            String loginId
    ) {
        MemberOrder memberOrder = request.toEntity(
                generateOrderName(List.copyOf(products.values())),
                generateOrderNumber(orderDateTime),
                orderDateTime,
                queryMemberService.findByLoginId(loginId),
                queryMemberAddressService.findById(request.getOrdererAddressId())
        );

        return memberOrderCommandOrderRepository.save(memberOrder);
    }

    private Order creatSubscribe(
            OrderSubscribeCreateRequestDto request,
            LocalDateTime orderDateTime,
            String loginId
    ) {
        SubscribeProduct subscribeProduct = queryProductService.findIssnByIsbn(request.getOrderProducts()
                .get(0));

        Subscribe subscribe = request.toEntity(
                // TODO 구독 상품의 주문 명 -> 구독 상품의 총칭 필요
                subscribeProduct.getISSN(),
                generateOrderNumber(orderDateTime),
                orderDateTime,
                queryMemberService.findByLoginId(loginId),
                queryMemberAddressService.findById(request.getOrdererAddressId()),
                generateNextRenewalDate(request.getExpectedDay(), orderDateTime),
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

    private void createUsePointHistory(long orderPoint, String loginId) {
        if (orderPoint != 0) {
            commandPointHistoryService.use(new PointHistoryRequestDto(
                    loginId,
                    orderPoint,
                    PointReasonCode.USE_ORDER
            ));
        }
    }

    private void createOrderStatusChangeLog(LocalDateTime orderDateTime, Order savedOrder) {
        OrderStatusChangeLog orderStatusChangeLog = OrderStatusChangeLog.create(
                savedOrder,
                orderDateTime,
                OrderStatusCode.ORDER
        );
        commandOrderStatusChangeLogRepository.save(orderStatusChangeLog);
    }

    private LocalDate generateNextRenewalDate(
            Integer expectedDay,
            LocalDateTime orderDateTime
    ) {
        LocalDate nextRenewalDate = orderDateTime.withDayOfMonth(expectedDay)
                .toLocalDate();
        if (nextRenewalDate.isAfter(ChronoLocalDate.from(orderDateTime))) {
            nextRenewalDate = nextRenewalDate.plusMonths(1);
        }
        return nextRenewalDate;
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
}
