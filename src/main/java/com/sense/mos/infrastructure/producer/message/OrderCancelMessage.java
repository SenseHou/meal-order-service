package com.sense.mos.infrastructure.producer.message;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderCancelMessage {
    private Long id;
    private BigDecimal amount;
}
