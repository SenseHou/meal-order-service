package com.sense.mos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sense.mos.builder.dto.OrderPaymentRequestDTOBuilder;
import com.sense.mos.builder.dto.PaymentResponseDTOBuilder;
import com.sense.mos.builder.entity.OrderBuilder;
import com.sense.mos.infrastructure.exceptions.OrderNoFundException;
import com.sense.mos.infrastructure.exceptions.OrderPaymentFailedException;
import com.sense.mos.infrastructure.exceptions.PaymentServiceUnavailableException;
import com.sense.mos.infrastructure.feigns.PaymentClient;
import com.sense.mos.infrastructure.feigns.dto.PaymentResponseDTO.PaymentResCode;
import com.sense.mos.infrastructure.producer.MessageProducer;
import com.sense.mos.infrastructure.producer.message.OrderCancelMessage;
import com.sense.mos.infrastructure.repository.OrderRepository;
import com.sense.mos.infrastructure.repository.entity.Order;
import com.sense.mos.service.model.OrderModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.sense.mos.common.ErrorCode.ALREADY_SHIPPING;
import static com.sense.mos.common.ErrorCode.LACK_OF_BALANCE;
import static com.sense.mos.common.ErrorCode.MEAL_PREPARING;
import static com.sense.mos.common.ErrorCode.ORDER_NOT_EXIT;
import static com.sense.mos.common.ErrorCode.PAYMENT_SYSTEM_UNAVAILABLE;
import static com.sense.mos.service.model.OrderStatus.CANCELED;
import static com.sense.mos.service.model.OrderStatus.PREPARING;
import static com.sense.mos.service.model.OrderStatus.SHIPPING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.mapping.Alias.ofNullable;

public class OrderServiceTest {

    private OrderService orderService;

    private OrderRepository stubOrderRepository;

    private MessageProducer stubMessageProducer;

    private PaymentClient stubPaymentClient;

    @BeforeEach
    protected void setUp() {
        stubOrderRepository = mock(OrderRepository.class);
        stubPaymentClient = mock(PaymentClient.class);
        stubMessageProducer = mock(MessageProducer.class);

        orderService = new OrderService(stubOrderRepository, stubMessageProducer, stubPaymentClient);
    }

    @Test
    void should_payment_order_successfully_when_payment_order_given_wechat_wallet_money_greater_than_total_price() {

        //Given:
        when(stubOrderRepository.findById(any())).thenReturn(Optional.of(OrderBuilder.withDefault().build()));
        when(stubPaymentClient.pay(any())).thenReturn(PaymentResponseDTOBuilder.withDefault().build());

        //When:
        orderService.payment(1L, OrderPaymentRequestDTOBuilder.withDefault().build());

        //Then:
        verify(stubOrderRepository, times(1)).saveAndFlush(any(Order.class));

    }


    @Test
    void should_payment_order_fail_when_payment_order_given_wechat_wallet_money_greater_than_total_price_but_order_no_fund() {

        //Given:
        when(stubOrderRepository.findById(any())).thenThrow(new OrderNoFundException(ORDER_NOT_EXIT, "订单不存在"));

        //When:
        OrderNoFundException exception = assertThrows(OrderNoFundException.class,
                () -> orderService.payment(1L, OrderPaymentRequestDTOBuilder.withDefault().build()));

        //Then:
        verify(stubOrderRepository, times(1)).findById(any());
        assertEquals("订单不存在", exception.getMessage());
        assertEquals(ORDER_NOT_EXIT, exception.getErrorCode());


    }

    @Test
    void should_payment_order_fail_when_payment_order_given_wechat_wallet_money_less_than_total_price() {

        //Given:
        when(stubOrderRepository.findById(any())).thenReturn(Optional.of(OrderBuilder.withDefault().build()));
        when(stubPaymentClient.pay(any())).thenReturn(PaymentResponseDTOBuilder.withDefault().withCode(
                PaymentResCode.LACK_OF_BALANCE.name()).build());

        //When:
        OrderPaymentFailedException exception = assertThrows(OrderPaymentFailedException.class,
                () -> orderService.payment(2L, OrderPaymentRequestDTOBuilder.withDefault().build()));

        //Then:
        verify(stubOrderRepository, times(1)).findById(any());
        verify(stubPaymentClient, times(1)).pay(any());
        assertEquals("余额不足！", exception.getMessage());
        assertEquals(LACK_OF_BALANCE, exception.getErrorCode());

    }

    @Test
    void should_payment_order_fail_when_payment_order_given_3rd_service_down() {

        //Given:
        when(stubOrderRepository.findById(any())).thenReturn(Optional.of(OrderBuilder.withDefault().build()));
        when(stubPaymentClient.pay(any())).thenReturn(null);

        //When:
        PaymentServiceUnavailableException exception = assertThrows(PaymentServiceUnavailableException.class,
                () -> orderService.payment(3L, OrderPaymentRequestDTOBuilder.withDefault().build()));

        //Then:
        verify(stubOrderRepository, times(1)).findById(any());
        verify(stubPaymentClient, times(1)).pay(any());
        assertEquals("支付平台错误，请稍后重试！", exception.getMessage());
        assertEquals(PAYMENT_SYSTEM_UNAVAILABLE, exception.getErrorCode());
    }

    @Test
    void should_cancel_order_successfully_when_cancel_order_given_meal_overtime() {

        //Given:
        when(stubOrderRepository.findById(any())).thenReturn(Optional.of(OrderBuilder.withDefault().build()));
        when(stubMessageProducer.send(any(OrderCancelMessage.class))).thenReturn(true);

        //When
        orderService.cancel(1L);

        // Then:
        verify(stubMessageProducer, times(1)).send(any(OrderCancelMessage.class));
        verify(stubOrderRepository, times(1)).saveAndFlush(any(Order.class));
    }


    @Test
    void should_cancel_order_fail_when_cancel_order_given_meal_preparing() {

        //Given:
        when(stubOrderRepository.findById(any())).thenReturn(
                Optional.of(OrderBuilder.withDefault()
                        .withReceivedAt(LocalDateTime.now().plusMinutes(14))
                        .withStatus(PREPARING).build()));
        when(stubMessageProducer.send(any(OrderCancelMessage.class))).thenReturn(true);

        //When,Then:
        OrderPaymentFailedException exception = assertThrows(OrderPaymentFailedException.class,
                () -> orderService.cancel(1L));
        assertEquals("备餐中，无法取消！", exception.getMessage());
        assertEquals(MEAL_PREPARING, exception.getErrorCode());

    }

    @Test
    void should_cancel_order_fail_when_cancel_order_given_meal_already_shipping() {

        //Given:
        when(stubOrderRepository.findById(any())).thenReturn(
                Optional.of(OrderBuilder.withDefault()
                        .withReceivedAt(LocalDateTime.now().plusMinutes(16))
                        .withStatus(SHIPPING).build()));
        when(stubMessageProducer.send(any(OrderCancelMessage.class))).thenReturn(true);

        //When,Then:
        OrderPaymentFailedException exception = assertThrows(OrderPaymentFailedException.class,
                () -> orderService.cancel(1L));
        assertEquals("配送中，无法取消！", exception.getMessage());
        assertEquals(ALREADY_SHIPPING, exception.getErrorCode());

    }

    @Test
    void should_cancel_order_successfully_when_cancel_order_given_meal_already_overtime_and_message_service_error() {

        //Given:
        when(stubOrderRepository.findById(any())).thenReturn(
                Optional.of(OrderBuilder.withDefault()
                        .withReceivedAt(LocalDateTime.now().plusMinutes(16))
                        .withStatus(PREPARING).build()));

        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        MessageProducer producer = new MessageProducer(rabbitTemplate, new ObjectMapper());
        doThrow(AmqpException.class).when(rabbitTemplate).convertAndSend(anyString(), ofNullable(any()));

        OrderService orderService = new OrderService(stubOrderRepository, producer, stubPaymentClient);

        //When
        OrderModel cancel = orderService.cancel(1L);

        // Then:
        assertEquals(cancel.getStatus(), CANCELED);

    }
}
