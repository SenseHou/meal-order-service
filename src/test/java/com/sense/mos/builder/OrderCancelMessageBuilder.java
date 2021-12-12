package com.sense.mos.builder;

import com.sense.mos.infrastructure.producer.message.OrderCancelMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCancelMessageBuilder {
    private final OrderCancelMessage dto = new OrderCancelMessage();

    public static OrderCancelMessageBuilder withDefault() {
        return new OrderCancelMessageBuilder()
                .withId(1L)
                .withAmount(BigDecimal.valueOf(10));
    }

    public OrderCancelMessage build() {
        return dto;
    }

    public OrderCancelMessageBuilder withId(Long id) {
        dto.setId(id);
        return this;
    }

    public OrderCancelMessageBuilder withAmount(BigDecimal amount) {
        dto.setAmount(amount);
        return this;
    }
}
