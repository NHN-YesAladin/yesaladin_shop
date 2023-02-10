package shop.yesaladin.shop.order.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
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
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.dto.OrderCreateResponseDto;
import shop.yesaladin.shop.order.dto.OrderMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderNonMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderSubscribeCreateRequestDto;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.order.service.inter.CommandOrderCouponService;
import shop.yesaladin.shop.order.service.inter.CommandOrderService;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointReasonCode;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;
import shop.yesaladin.shop.product.dto.SubscribeProductOrderResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

class CommandOrderServiceImplTest {

    CommandOrderService commandOrderService;
    CommandOrderRepository<NonMemberOrder> nonMemberOrderCommandOrderRepository;
    CommandOrderRepository<MemberOrder> memberOrderCommandOrderRepository;
    CommandOrderRepository<Subscribe> subscribeCommandOrderRepository;
    QueryOrderRepository queryOrderRepository;

    CommandOrderStatusChangeLogRepository commandOrderStatusChangeLogRepository;
    CommandOrderProductRepository commandOrderProductRepository;
    CommandPointHistoryService commandPointHistoryService;
    CommandOrderCouponService commandOrderCouponService;
    CommandProductService commandProductService;
    QueryMemberAddressService queryMemberAddressService;
    QueryProductService queryProductService;
    QueryMemberService queryMemberService;

    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-10T00:00:00.000Z"),
            ZoneId.of("UTC")
    );
    Member member;
    MemberAddress memberAddress;
    NonMemberOrder nonMemberOrder;
    MemberOrder memberOrder;
    Subscribe subscribe;
    SubscribeProduct subscribeProduct;
    SubscribeProductOrderResponseDto subscribeProductOrder;

    String ordererName = "김몽머";
    String ordererPhoneNumber = "01012341234";
    String ordererAddress = "서울특별시 구로구 디지털로26길 72 (구로동, NHN KCP)";
    LocalDate expectedShippingDate = LocalDate.of(2023, 1, 5);
    String recipientName = "김몽대";
    String recipientPhoneNumber = "01029482743";
    List<ProductOrderRequestDto> orderProducts;
    long nonMemberProductTotalAmount = 10000L;
    long productTotalAmount = 9000L;
    int shippingFee = 3000;
    int wrappingFee = 0;
    Long ordererAddressId = 1L;
    List<String> orderCoupons = List.of("0001", "0002");
    long orderPoint = 1000L;
    Integer expectedDay = 10;
    Integer intervalMonth = 6;

    List<Product> subscribeProducts;
    List<Product> nonSubscribeProducts;

    @BeforeEach
    void setUp() {
        //noinspection unchecked
        nonMemberOrderCommandOrderRepository = (CommandOrderRepository<NonMemberOrder>) Mockito.mock(
                CommandOrderRepository.class);
        //noinspection unchecked
        memberOrderCommandOrderRepository = (CommandOrderRepository<MemberOrder>) Mockito.mock(
                CommandOrderRepository.class);
        //noinspection unchecked
        subscribeCommandOrderRepository = (CommandOrderRepository<Subscribe>) Mockito.mock(
                CommandOrderRepository.class);
        queryOrderRepository = Mockito.mock(QueryOrderRepository.class);

        commandOrderStatusChangeLogRepository = Mockito.mock(CommandOrderStatusChangeLogRepository.class);
        commandOrderProductRepository = Mockito.mock(CommandOrderProductRepository.class);
        commandPointHistoryService = Mockito.mock(CommandPointHistoryService.class);
        commandOrderCouponService = Mockito.mock(CommandOrderCouponService.class);
        commandProductService = Mockito.mock(CommandProductService.class);
        queryMemberAddressService = Mockito.mock(QueryMemberAddressService.class);
        queryProductService = Mockito.mock(QueryProductService.class);
        queryMemberService = Mockito.mock(QueryMemberService.class);

        commandOrderService = new CommandOrderServiceImpl(
                nonMemberOrderCommandOrderRepository,
                memberOrderCommandOrderRepository,
                subscribeCommandOrderRepository,
                queryOrderRepository,
                commandOrderStatusChangeLogRepository,
                commandOrderProductRepository,
                commandOrderCouponService,
                commandPointHistoryService,
                commandProductService,
                queryMemberAddressService,
                queryProductService,
                queryMemberService,
                clock
        );
        member = DummyMember.memberWithId();
        memberAddress = DummyMemberAddress.addressWithId(member);
        subscribeProduct = DummySubscribeProduct.subscribeProduct();

        nonMemberOrder = DummyOrder.nonMemberOrderWithId();
        memberOrder = DummyOrder.memberOrderWithId(member, memberAddress);
        subscribe = DummyOrder.subscribeWithId(member, memberAddress, subscribeProduct);

        setRequiredData();
    }

    @Test
    @DisplayName("비회원 주문 생성 실패 - [상품] 주문 상품의 구매가 불가능한 경우")
    void createNonMemberOrders_fail_productNotAvailableToOrder() {
        //given
        OrderNonMemberCreateRequestDto request = getNonMemberOrderRequest();

        String errorMessage = "Product is not available to order";
        Mockito.when(commandProductService.orderProducts(any()))
                .thenThrow(new ClientException(ErrorCode.BAD_REQUEST, errorMessage));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createNonMemberOrders(request)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.BAD_REQUEST);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(commandProductService, times(1)).orderProducts(any());
        verify(nonMemberOrderCommandOrderRepository, never()).save(any());
        verify(commandOrderProductRepository, never()).save(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("비회원 주문 생성 성공")
    void createNonMemberOrders_success() {
        //given
        OrderNonMemberCreateRequestDto request = getNonMemberOrderRequest();

        Map<String, Product> mapProduct = getMapProducts(nonSubscribeProducts);
        Mockito.when(commandProductService.orderProducts(any())).thenReturn(mapProduct);
        Mockito.when(nonMemberOrderCommandOrderRepository.save(any())).thenReturn(nonMemberOrder);
        OrderProduct orderProduct = getNonMemberOrderProducts(
                mapProduct,
                request,
                nonMemberOrder
        ).get(0);
        Mockito.when(commandOrderProductRepository.save(any())).thenReturn(orderProduct);
        OrderStatusChangeLog orderStatusChangeLog = getOrderStatusChangeLog(nonMemberOrder);
        Mockito.when(commandOrderStatusChangeLogRepository.save(any()))
                .thenReturn(orderStatusChangeLog);
        //when
        OrderCreateResponseDto result = commandOrderService.createNonMemberOrders(request);

        //then
        assertThat(result.getWrappingFee()).isEqualTo(wrappingFee);
        assertThat(result.getShippingFee()).isEqualTo(shippingFee);
        assertThat(result.getTotalAmount()).isEqualTo(nonMemberProductTotalAmount);

        verify(commandProductService, times(1)).orderProducts(any());
        verify(nonMemberOrderCommandOrderRepository, times(1)).save(any());
        verify(commandOrderProductRepository, times(5)).save(any());
        verify(commandOrderStatusChangeLogRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("회원 주문 생성 실패 - [상품] 주문 상품이 존재하지 않는 경우")
    void createMemberOrders_fail_productNotFound() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();

        OrderMemberCreateRequestDto request = getMemberOrderRequest();

        String errorMessage = "Product is not available to order";
        Mockito.when(commandProductService.orderProducts(any()))
                .thenThrow(new ClientException(ErrorCode.BAD_REQUEST, errorMessage));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createMemberOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.BAD_REQUEST);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(commandProductService, times(1)).orderProducts(any());
        verify(queryMemberService, never()).findByLoginId(loginId);
        verify(queryMemberAddressService, never()).findById(addressId);
        verify(memberOrderCommandOrderRepository, never()).save(any());
        verify(commandOrderProductRepository, never()).save(any());
        verify(commandPointHistoryService, never()).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원 주문 생성 실패 - [회원] 존재하지 않는 회원인 경우")
    void createMemberOrders_fail_memberNotFound() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();

        OrderMemberCreateRequestDto request = getMemberOrderRequest();

        Map<String, Product> mapProduct = getMapProducts(nonSubscribeProducts);
        Mockito.when(commandProductService.orderProducts(any())).thenReturn(mapProduct);

        String errorMessage = "Member not found with loginId : " + loginId;
        Mockito.when(queryMemberService.findByLoginId(anyString()))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, errorMessage));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createMemberOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(commandProductService, times(1)).orderProducts(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, never()).findById(addressId);
        verify(memberOrderCommandOrderRepository, never()).save(any());
        verify(commandOrderProductRepository, never()).save(any());
        verify(commandPointHistoryService, never()).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원 주문 생성 실패 - [배송지] 존재하지 않는 배송지인 경우")
    void createMemberOrders_fail_memberAddressNotFound() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();

        OrderMemberCreateRequestDto request = getMemberOrderRequest();

        Map<String, Product> mapProduct = getMapProducts(nonSubscribeProducts);
        Mockito.when(commandProductService.orderProducts(any())).thenReturn(mapProduct);
        Mockito.when(queryMemberService.findByLoginId(anyString())).thenReturn(member);

        String errorMessage = "MemberAddress not found with id : " + memberAddress.getId();
        Mockito.when(queryMemberAddressService.findById(anyLong()))
                .thenThrow(new ClientException(ErrorCode.ADDRESS_NOT_FOUND, errorMessage));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createMemberOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(commandProductService, times(1)).orderProducts(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, times(1)).findById(addressId);
        verify(memberOrderCommandOrderRepository, never()).save(any());
        verify(commandOrderProductRepository, never()).save(any());
        verify(commandPointHistoryService, never()).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원 주문 생성 실패 - [포인트] 존재하지 않는 회원인 경우")
    void createMemberOrders_fail_point_memberNotFound() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();

        OrderMemberCreateRequestDto request = getMemberOrderRequest();

        Map<String, Product> mapProduct = getMapProducts(nonSubscribeProducts);
        Mockito.when(commandProductService.orderProducts(any())).thenReturn(mapProduct);
        Mockito.when(queryMemberService.findByLoginId(anyString())).thenReturn(member);
        Mockito.when(queryMemberAddressService.findById(anyLong())).thenReturn(memberAddress);
        Mockito.when(memberOrderCommandOrderRepository.save(any())).thenReturn(memberOrder);
        OrderProduct orderProduct = getMemberOrderProducts(
                mapProduct,
                request,
                memberOrder
        ).get(0);
        Mockito.when(commandOrderProductRepository.save(any())).thenReturn(orderProduct);
        Mockito.when(commandPointHistoryService.use(any()))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createMemberOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);

        verify(commandProductService, times(1)).orderProducts(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, times(1)).findById(addressId);
        verify(commandOrderCouponService, times(1)).createOrderCoupons(any(), any());
        verify(memberOrderCommandOrderRepository, times(1)).save(any());
        verify(commandOrderProductRepository, times(5)).save(any());
        verify(commandPointHistoryService, times(1)).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원 주문 생성 실패 - [포인트] 소지한 포인트보다 더 많이 사용한 경우")
    void createMemberOrders_fail_pointOverUse() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();
        OrderMemberCreateRequestDto request = getMemberOrderRequest();

        Map<String, Product> mapProduct = getMapProducts(nonSubscribeProducts);
        Mockito.when(commandProductService.orderProducts(any())).thenReturn(mapProduct);
        Mockito.when(queryMemberService.findByLoginId(anyString())).thenReturn(member);
        Mockito.when(queryMemberAddressService.findById(anyLong())).thenReturn(memberAddress);
        Mockito.when(memberOrderCommandOrderRepository.save(any())).thenReturn(memberOrder);
        OrderProduct orderProduct = getMemberOrderProducts(
                mapProduct,
                request,
                memberOrder
        ).get(0);
        Mockito.when(commandOrderProductRepository.save(any())).thenReturn(orderProduct);

        String errorMessage = "Use over point with point : " + orderPoint;
        Mockito.when(commandPointHistoryService.use(any()))
                .thenThrow(new ClientException(ErrorCode.POINT_OVER_USE, errorMessage));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createMemberOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.POINT_OVER_USE);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(commandProductService, times(1)).orderProducts(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, times(1)).findById(addressId);
        verify(commandOrderCouponService, times(1)).createOrderCoupons(any(), any());
        verify(memberOrderCommandOrderRepository, times(1)).save(any());
        verify(commandOrderProductRepository, times(5)).save(any());
        verify(commandPointHistoryService, times(1)).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원 주문 생성 성공")
    void createMemberOrders_success() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();
        OrderMemberCreateRequestDto request = getMemberOrderRequest();

        Map<String, Product> mapProduct = getMapProducts(nonSubscribeProducts);
        Mockito.when(commandProductService.orderProducts(any())).thenReturn(mapProduct);
        Mockito.when(queryMemberService.findByLoginId(any())).thenReturn(member);
        Mockito.when(queryMemberAddressService.findById(addressId)).thenReturn(memberAddress);
        Mockito.when(memberOrderCommandOrderRepository.save(any())).thenReturn(memberOrder);
        OrderProduct orderProduct = getMemberOrderProducts(
                mapProduct,
                request,
                memberOrder
        ).get(0);
        Mockito.when(commandOrderProductRepository.save(any())).thenReturn(orderProduct);
        PointHistoryResponseDto pointHistoryResponse = new PointHistoryResponseDto(
                1L,
                orderPoint,
                LocalDateTime.now(),
                PointCode.USE,
                PointReasonCode.USE_ORDER
        );
        Mockito.when(commandPointHistoryService.use(any())).thenReturn(pointHistoryResponse);
        OrderStatusChangeLog orderStatusChangeLog = getOrderStatusChangeLog(nonMemberOrder);
        Mockito.when(commandOrderStatusChangeLogRepository.save(any()))
                .thenReturn(orderStatusChangeLog);
        //when
        OrderCreateResponseDto result = commandOrderService.createMemberOrders(
                request,
                loginId
        );

        //then
        assertThat(result.getWrappingFee()).isEqualTo(wrappingFee);
        assertThat(result.getShippingFee()).isEqualTo(shippingFee);
        assertThat(result.getTotalAmount()).isEqualTo(productTotalAmount);

        verify(commandProductService, times(1)).orderProducts(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, times(1)).findById(addressId);
        verify(commandOrderCouponService, times(1)).createOrderCoupons(any(), any());
        verify(memberOrderCommandOrderRepository, times(1)).save(any());
        verify(commandOrderProductRepository, times(5)).save(any());
        verify(commandPointHistoryService, times(1)).use(any());
        verify(commandOrderStatusChangeLogRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("정기구독 생성 실패 - [상품] 존재하지 않는 상품인 경우")
    void createSubscribeOrders_fail_productNotFound() {
        //given
        String loginId = member.getLoginId();
        OrderSubscribeCreateRequestDto request = getSubscribeRequest();

        Mockito.when(queryMemberService.findByLoginId(any())).thenReturn(member);
        Mockito.when(queryMemberAddressService.findById(anyLong())).thenReturn(memberAddress);

        String isbn = subscribeProducts.get(0).getIsbn();
        String errorMessage = "Product with isbn(" + isbn + ") is not a subscribe product";
        Mockito.when(queryProductService.getIssnByOrderProduct(any()))
                .thenThrow(new ClientException(
                        ErrorCode.PRODUCT_NOT_SUBSCRIBE_PRODUCT,
                        errorMessage
                ));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createSubscribeOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_SUBSCRIBE_PRODUCT);
        assertThat(result.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("정기구독 생성 실패 - [상품] 정기구독 상품이 아닌 경우")
    void createSubscribeOrders_fail_productIsNotSubscribeProduct() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();
        OrderSubscribeCreateRequestDto request = getSubscribeRequest();

        String isbn = subscribeProducts.get(0).getIsbn();
        String errorMessage = "Product with isbn(" + isbn + ") is not a subscribe product";
        Mockito.when(queryProductService.getIssnByOrderProduct(any()))
                .thenThrow(new ClientException(
                        ErrorCode.PRODUCT_NOT_SUBSCRIBE_PRODUCT,
                        errorMessage
                ));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createSubscribeOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_SUBSCRIBE_PRODUCT);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(queryProductService, times(1)).getIssnByOrderProduct(any());
        verify(queryMemberService, never()).findByLoginId(loginId);
        verify(queryMemberAddressService, never()).findById(addressId);
        verify(commandOrderCouponService, never()).createOrderCoupons(any(), any());
        verify(subscribeCommandOrderRepository, never()).save(any());
        verify(commandPointHistoryService, never()).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("정기구독 생성 실패 - [회원] 존재하지 않는 회원인 경우")
    void createSubscribeOrders_fail_memberNotFound() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();
        OrderSubscribeCreateRequestDto request = getSubscribeRequest();

        Mockito.when(queryProductService.getIssnByOrderProduct(any()))
                .thenReturn(subscribeProductOrder);

        String errorMessage = "Member not found with loginId : " + loginId;
        Mockito.when(queryMemberService.findByLoginId(loginId))
                .thenThrow(new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        errorMessage
                ));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createSubscribeOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(queryProductService, times(1)).getIssnByOrderProduct(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, never()).findById(addressId);
        verify(commandOrderCouponService, never()).createOrderCoupons(any(), any());
        verify(subscribeCommandOrderRepository, never()).save(any());
        verify(commandPointHistoryService, never()).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("정기구독 생성 실패 - [배송지] 존재하지 않는 배송지인 경우")
    void createSubscribeOrders_fail_memberAddressNotFound() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();
        OrderSubscribeCreateRequestDto request = getSubscribeRequest();

        Mockito.when(queryProductService.getIssnByOrderProduct(any()))
                .thenReturn(subscribeProductOrder);
        Mockito.when(queryMemberService.findByLoginId(loginId)).thenReturn(member);

        long memberAddressId = memberAddress.getId();
        String errorMessage = "MemberAddress not found with id : " + memberAddressId;
        Mockito.when(queryMemberAddressService.findById(memberAddressId))
                .thenThrow(new ClientException(
                        ErrorCode.ADDRESS_NOT_FOUND,
                        errorMessage
                ));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createSubscribeOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(queryProductService, times(1)).getIssnByOrderProduct(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, times(1)).findById(addressId);
        verify(commandOrderCouponService, times(0)).createOrderCoupons(any(), any());
        verify(subscribeCommandOrderRepository, never()).save(any());
        verify(commandPointHistoryService, never()).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("정기구독 생성 실패 - [포인트] 존재하지 않는 회원인 경우")
    void createSubscribeOrders_fail_point_memberNotFound() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();
        OrderSubscribeCreateRequestDto request = getSubscribeRequest();

        Mockito.when(queryProductService.getIssnByOrderProduct(any()))
                .thenReturn(subscribeProductOrder);
        Mockito.when(queryMemberService.findByLoginId(anyString())).thenReturn(member);
        Mockito.when(queryMemberAddressService.findById(anyLong())).thenReturn(memberAddress);
        Mockito.when(subscribeCommandOrderRepository.save(any())).thenReturn(subscribe);

        String errorMessage = "Member not found with loginId : " + loginId;
        Mockito.when(commandPointHistoryService.use(any()))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, errorMessage));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createSubscribeOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(queryProductService, times(1)).getIssnByOrderProduct(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, times(1)).findById(addressId);
        verify(commandOrderCouponService, times(0)).createOrderCoupons(any(), any());
        verify(subscribeCommandOrderRepository, times(1)).save(any());
        verify(commandPointHistoryService, times(1)).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("정기구독 생성 실패 - [포인트] 소지한 포인트보다 더 많이 사용한 경우")
    void createSubscribeOrders_fail_pointOverUse() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();
        OrderSubscribeCreateRequestDto request = getSubscribeRequest();

        Mockito.when(queryProductService.getIssnByOrderProduct(any()))
                .thenReturn(subscribeProductOrder);
        Mockito.when(queryMemberService.findByLoginId(anyString())).thenReturn(member);
        Mockito.when(queryMemberAddressService.findById(anyLong())).thenReturn(memberAddress);
        Mockito.when(subscribeCommandOrderRepository.save(any())).thenReturn(subscribe);

        String errorMessage = "Use over point with point : " + orderPoint;
        Mockito.when(commandPointHistoryService.use(any()))
                .thenThrow(new ClientException(ErrorCode.POINT_OVER_USE, errorMessage));

        //when
        ClientException result = assertThrows(
                ClientException.class,
                () -> commandOrderService.createSubscribeOrders(request, loginId)
        );

        //then
        assertThat(result.getResponseStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.POINT_OVER_USE);
        assertThat(result.getMessage()).isEqualTo(errorMessage);

        verify(queryProductService, times(1)).getIssnByOrderProduct(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, times(1)).findById(addressId);
        verify(commandOrderCouponService, times(0)).createOrderCoupons(any(), any());
        verify(subscribeCommandOrderRepository, times(1)).save(any());
        verify(commandPointHistoryService, times(1)).use(any());
        verify(commandOrderStatusChangeLogRepository, never()).save(any());
    }

    @Test
    @DisplayName("정기구독 생성 성공")
    void createSubscribeOrders_success() {
        //given
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();
        OrderSubscribeCreateRequestDto request = getSubscribeRequest();

        Mockito.when(queryProductService.getIssnByOrderProduct(any()))
                .thenReturn(subscribeProductOrder);
        Mockito.when(queryMemberService.findByLoginId(any())).thenReturn(member);
        Mockito.when(queryMemberAddressService.findById(anyLong())).thenReturn(memberAddress);
        Mockito.when(subscribeCommandOrderRepository.save(any())).thenReturn(subscribe);

        PointHistoryResponseDto pointHistoryResponse = new PointHistoryResponseDto(
                1L,
                orderPoint,
                LocalDateTime.now(),
                PointCode.USE,
                PointReasonCode.USE_ORDER
        );
        Mockito.when(commandPointHistoryService.use(any())).thenReturn(pointHistoryResponse);
        OrderStatusChangeLog orderStatusChangeLog = getOrderStatusChangeLog(nonMemberOrder);
        Mockito.when(commandOrderStatusChangeLogRepository.save(any()))
                .thenReturn(orderStatusChangeLog);
        //when
        OrderCreateResponseDto result = commandOrderService.createSubscribeOrders(
                request,
                loginId
        );

        //then
        assertThat(result.getWrappingFee()).isEqualTo(wrappingFee);
        assertThat(result.getShippingFee()).isEqualTo(shippingFee);
        assertThat(result.getTotalAmount()).isEqualTo(productTotalAmount);

        verify(queryProductService, times(1)).getIssnByOrderProduct(any());
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryMemberAddressService, times(1)).findById(addressId);
        verify(commandOrderCouponService, times(1)).createOrderCoupons(any(), any());
        verify(subscribeCommandOrderRepository, times(1)).save(any());
        verify(commandPointHistoryService, times(1)).use(any());
        verify(commandOrderStatusChangeLogRepository, times(1)).save(any());
    }

    private OrderNonMemberCreateRequestDto getNonMemberOrderRequest() {
        return new OrderNonMemberCreateRequestDto(
                expectedShippingDate,
                orderProducts,
                nonMemberProductTotalAmount,
                shippingFee,
                wrappingFee,
                recipientName,
                recipientPhoneNumber,
                ordererName,
                ordererPhoneNumber,
                ordererAddress
        );
    }

    private OrderMemberCreateRequestDto getMemberOrderRequest() {
        return new OrderMemberCreateRequestDto(
                expectedShippingDate,
                orderProducts,
                productTotalAmount,
                shippingFee,
                wrappingFee,
                recipientName,
                recipientPhoneNumber,
                ordererAddressId,
                orderCoupons,
                orderPoint
        );
    }

    private OrderSubscribeCreateRequestDto getSubscribeRequest() {
        return new OrderSubscribeCreateRequestDto(
                expectedShippingDate,
                orderProducts,
                productTotalAmount,
                shippingFee,
                wrappingFee,
                recipientName,
                recipientPhoneNumber,
                ordererAddressId,
                orderCoupons,
                orderPoint,
                expectedDay,
                intervalMonth
        );
    }

    private void setRequiredData() {
        orderProducts = new ArrayList<>();
        subscribeProducts = new ArrayList<>();
        nonSubscribeProducts = new ArrayList<>();

        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("12342354")
                .build();

        //isbn : 13자리 숫자 스트링
        String isbn = "152374859182";
        for (int i = 0; i < 5; i++) {
            int quantity = i + 1;
            orderProducts.add(new ProductOrderRequestDto(isbn + i, quantity));
            nonSubscribeProducts.add(DummyProduct.dummy(isbn + i, null, null, null, null));
        }
        Product product = DummyProduct.dummy(isbn + 7, subscribeProduct, null, null, null);
        subscribeProducts.add(product);
        subscribeProductOrder = new SubscribeProductOrderResponseDto(product);
    }

    private Map<String, Product> getMapProducts(List<Product> products) {
        return products.stream().collect(Collectors.toMap(Product::getIsbn, product -> product));
    }

    private List<OrderProduct> getNonMemberOrderProducts(
            Map<String, Product> products,
            OrderNonMemberCreateRequestDto request,
            NonMemberOrder order
    ) {
        return request.getOrderProducts()
                .stream()
                .map(orderProduct -> OrderProduct.builder()
                        .quantity(orderProduct.getQuantity())
                        .product(products.get(orderProduct.getIsbn()))
                        .order(order)
                        .build())
                .collect(Collectors.toList());
    }

    private List<OrderProduct> getMemberOrderProducts(
            Map<String, Product> products,
            OrderMemberCreateRequestDto request,
            MemberOrder order
    ) {
        return request.getOrderProducts()
                .stream()
                .map(orderProduct -> OrderProduct.builder()
                        .quantity(orderProduct.getQuantity())
                        .product(products.get(orderProduct.getIsbn()))
                        .order(order)
                        .build())
                .collect(Collectors.toList());
    }

    private OrderStatusChangeLog getOrderStatusChangeLog(Order order) {
        return OrderStatusChangeLog.create(order, LocalDateTime.now(), OrderStatusCode.ORDER);
    }
}