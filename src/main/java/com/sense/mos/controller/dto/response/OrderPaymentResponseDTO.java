package com.sense.mos.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentResponseDTO {
    private String code;
    private String message;

    public static OrderPaymentResponseDTO success() {
        return new OrderPaymentResponseDTO("SUCCESS", "支付成功");
    }
}
