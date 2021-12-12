package com.sense.mos.builder.dto;

import com.sense.mos.infrastructure.feigns.dto.PaymentResponseDTO;
import com.sense.mos.infrastructure.feigns.dto.PaymentResponseDTO.PaymentResCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentResponseDTOBuilder {
    private final PaymentResponseDTO dto = new PaymentResponseDTO();

    public static PaymentResponseDTOBuilder withDefault() {
        return new PaymentResponseDTOBuilder()
                .withCode(PaymentResCode.SUCCESS.name());
    }

    public PaymentResponseDTO build() {
        return dto;
    }

    public PaymentResponseDTOBuilder withCode(String code) {
        dto.setCode(code);
        return this;
    }
}
