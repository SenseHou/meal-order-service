package com.sense.mos.controller;

import com.sense.mos.base.IntegrationTest;
import com.sense.mos.builder.dto.OrderCancelResponseDTOBuilder;
import com.sense.mos.builder.dto.PaymentRequestDTOBuilder;
import com.sense.mos.infrastructure.exceptions.OrderNoFundException;
import com.sense.mos.infrastructure.exceptions.OrderPaymentFailedException;
import com.sense.mos.infrastructure.exceptions.PaymentServiceUnavailableException;
import com.sense.mos.service.OrderService;
import com.sense.mos.service.model.OrderModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.sense.mos.common.ErrorCode.ALREADY_SHIPPING;
import static com.sense.mos.common.ErrorCode.LACK_OF_BALANCE;
import static com.sense.mos.common.ErrorCode.MEAL_PREPARING;
import static com.sense.mos.common.ErrorCode.ORDER_NOT_EXIT;
import static com.sense.mos.common.ErrorCode.PAYMENT_SYSTEM_UNAVAILABLE;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends IntegrationTest {

    @MockBean
    private OrderService orderService;

    @Test
    void should_payment_order_successfully_when_payment_order_given_wechat_wallet_money_greater_than_total_price()
            throws Exception {
        //Given:
        doNothing().when(orderService).payment(any(), any());

        //When:
        mockMvc.perform(post("/meal-order-contracts/{cid}/payment/confirm", 1L,
                        PaymentRequestDTOBuilder.withDefault().build()))
                //Then:
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("支付成功")));
    }


    @Test
    void should_payment_order_fail_when_payment_order_given_wechat_wallet_money_greater_than_total_price_but_order_no_fund()
            throws Exception {

        //Given:
        doThrow(new OrderNoFundException(ORDER_NOT_EXIT, "订单不存在")).when(orderService).payment(any(), any());

        //When:
        mockMvc.perform(post("/meal-order-contracts/{cid}/payment/confirm", 1L,
                        PaymentRequestDTOBuilder.withDefault().build()))
                //Then:
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("ORDER_NOT_EXIT")))
                .andExpect(jsonPath("$.message", is("订单不存在")));
    }


    @Test
    void should_payment_order_fail_when_payment_order_given_wechat_wallet_money_less_than_total_price()
            throws Exception {

        //Given:
        doThrow(new OrderPaymentFailedException(LACK_OF_BALANCE, "余额不足！")).when(orderService).payment(any(), any());

        //When:
        mockMvc.perform(post("/meal-order-contracts/{cid}/payment/confirm", 2L,
                        PaymentRequestDTOBuilder.withDefault().build()))
                //Then:
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is("LACK_OF_BALANCE")))
                .andExpect(jsonPath("$.message", is("余额不足！")));
    }


    @Test
    void should_payment_order_fail_when_payment_order_given_3rd_service_down()
            throws Exception {

        //Given:
        doThrow(new PaymentServiceUnavailableException(PAYMENT_SYSTEM_UNAVAILABLE, "支付平台错误，请稍后重试！")).when(orderService)
                .payment(any(), any());

        //When:
        mockMvc.perform(post("/meal-order-contracts/{cid}/payment/confirm", 3L,
                        PaymentRequestDTOBuilder.withDefault().build()))
                //Then:
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code", is("PAYMENT_SYSTEM_UNAVAILABLE")))
                .andExpect(jsonPath("$.message", is("支付平台错误，请稍后重试！")));
    }

    @Test
    void should_cancel_order_successfully_when_cancel_order_given_meal_overtime() throws Exception {
        //Given:
        when(orderService.cancel(any())).thenReturn(new OrderModel());

        //When:
        mockMvc.perform(post("/meal-order-contracts/{cid}/cancel/confirm", 1L,
                        OrderCancelResponseDTOBuilder.withDefault().build()))
                //Then:
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("取消成功")));
    }

    @Test
    void should_cancel_order_fail_when_cancel_order_given_meal_preparing() throws Exception {
        //Given:
        doThrow(new OrderPaymentFailedException(MEAL_PREPARING, "备餐中，无法取消！")).when(orderService)
                .cancel(any());

        //When:
        mockMvc.perform(post("/meal-order-contracts/{cid}/cancel/confirm", 1L,
                        OrderCancelResponseDTOBuilder.withDefault().build()))
                //Then:
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(MEAL_PREPARING.name())))
                .andExpect(jsonPath("$.message", is("备餐中，无法取消！")));
    }

    @Test
    void should_cancel_order_fail_when_cancel_order_given_meal_already_shipping() throws Exception {
        //Given:
        doThrow(new OrderPaymentFailedException(ALREADY_SHIPPING, "配送中，无法取消！")).when(orderService)
                .cancel(any());

        //When:
        mockMvc.perform(post("/meal-order-contracts/{cid}/cancel/confirm", 1L,
                        OrderCancelResponseDTOBuilder.withDefault().build()))
                //Then:
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", is(ALREADY_SHIPPING.name())))
                .andExpect(jsonPath("$.message", is("配送中，无法取消！")));
    }

}
