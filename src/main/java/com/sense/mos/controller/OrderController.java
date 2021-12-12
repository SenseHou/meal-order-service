package com.sense.mos.controller;


import com.sense.mos.controller.dto.request.OrderPaymentRequestDTO;
import com.sense.mos.controller.dto.response.OrderCancelResponseDTO;
import com.sense.mos.controller.dto.response.OrderPaymentResponseDTO;
import com.sense.mos.service.OrderService;
import com.sense.mos.service.model.OrderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meal-order-contracts/")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("{cid}/payment/confirm")
    public ResponseEntity<OrderPaymentResponseDTO> paymentConfirm(@PathVariable("cid") Long cid,
            OrderPaymentRequestDTO request) {
        orderService.payment(cid, request);
        return ResponseEntity.ok(OrderPaymentResponseDTO.success());
    }

    @PostMapping("{cid}/cancel/confirm")
    public ResponseEntity<OrderCancelResponseDTO> cancelOrder(@PathVariable("cid") Long cid) {
        OrderModel cancel = orderService.cancel(cid);
        return ResponseEntity.ok(OrderCancelResponseDTO.success());
    }
}
