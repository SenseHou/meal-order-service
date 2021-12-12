package com.sense.mos.infrastructure.feigns;

import com.sense.mos.infrastructure.feigns.dto.PaymentRequestDTO;
import com.sense.mos.infrastructure.feigns.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${payment.name}", url = "${payment.url}")
public interface PaymentClient {

    @PostMapping("/wechat-pay")
    PaymentResponseDTO pay(@RequestBody PaymentRequestDTO payRequestDTOS);
}
