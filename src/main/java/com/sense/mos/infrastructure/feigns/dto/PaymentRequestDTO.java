package com.sense.mos.infrastructure.feigns.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaymentRequestDTO {
    private BigDecimal amount;

    private String fromAccount;

    private String toAccount;
}
