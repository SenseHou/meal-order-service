package com.sense.mos.builder.dto;

import com.sense.mos.controller.dto.request.OrderPaymentRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderPaymentRequestDTOBuilder {
    private final OrderPaymentRequestDTO dto = new OrderPaymentRequestDTO();

    public static OrderPaymentRequestDTOBuilder withDefault() {
        return new OrderPaymentRequestDTOBuilder()
                .withFromAccount(UUID.randomUUID().toString())
                .withToAccount(UUID.randomUUID().toString());
    }

    public OrderPaymentRequestDTO build() {
        return dto;
    }

    public OrderPaymentRequestDTOBuilder withFromAccount(String fromAccount) {
        dto.setFromAccount(fromAccount);
        return this;
    }

    public OrderPaymentRequestDTOBuilder withToAccount(String toAccount) {
        dto.setToAccount(toAccount);
        return this;
    }
}
