package com.sense.mos.infrastructure.Feigns;

import com.sense.mos.base.IntegrationTest;
import com.sense.mos.builder.dto.PaymentRequestDTOBuilder;
import com.sense.mos.infrastructure.feigns.PaymentClient;
import com.sense.mos.infrastructure.feigns.dto.PaymentResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static com.sense.mos.infrastructure.feigns.dto.PaymentResponseDTO.PaymentResCode.LACK_OF_BALANCE;
import static com.sense.mos.infrastructure.feigns.dto.PaymentResponseDTO.PaymentResCode.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentClientTest extends IntegrationTest {

    @Autowired
    PaymentClient paymentClient;

    @Test
    void should_payment_order_successfully_when_payment_order_given_wechat_wallet_money_greater_than_total_price() {
        PaymentResponseDTO pay = paymentClient.pay(PaymentRequestDTOBuilder.withDefault().build());
        assertEquals(pay.getCode(), SUCCESS.name());
    }

    @Test
    void should_payment_order_fail_when_payment_order_given_wechat_wallet_money_less_than_total_price() {
        PaymentResponseDTO pay = paymentClient.pay(
                PaymentRequestDTOBuilder.withDefault().withAmount(BigDecimal.valueOf(41)).build());
        assertEquals(pay.getCode(), LACK_OF_BALANCE.name());
    }
}
