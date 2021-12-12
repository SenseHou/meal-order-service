package com.sense.mos.infrastructure.feigns.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {
    private String code;

    public boolean isSuccess() {
        return PaymentResCode.SUCCESS.name().equals(code);
    }
    public boolean isLackOfBalance() {
        return PaymentResCode.LACK_OF_BALANCE.name().equals(code);
    }

    public enum PaymentResCode {
        SUCCESS, LACK_OF_BALANCE
    }
}
