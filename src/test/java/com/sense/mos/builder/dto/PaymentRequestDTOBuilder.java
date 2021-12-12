package com.sense.mos.builder.dto;

import com.sense.mos.infrastructure.feigns.dto.PaymentRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequestDTOBuilder {
    private final PaymentRequestDTO dto = new PaymentRequestDTO();

    public static PaymentRequestDTOBuilder withDefault() {
        return new PaymentRequestDTOBuilder()
                .withAmount(BigDecimal.valueOf(34.5))
                .withFromAccount(UUID.randomUUID().toString())
                .withToAccount(UUID.randomUUID().toString());
    }

    public PaymentRequestDTO build() {
        return dto;
    }

    public PaymentRequestDTOBuilder withFromAccount(String fromAccount) {
        dto.setFromAccount(fromAccount);
        return this;
    }

    public PaymentRequestDTOBuilder withToAccount(String toAccount) {
        dto.setToAccount(toAccount);
        return this;
    }

    public PaymentRequestDTOBuilder withAmount(BigDecimal amount) {
        dto.setAmount(amount);
        return this;
    }
}
