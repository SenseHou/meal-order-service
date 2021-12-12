package com.sense.mos.service;

import com.sense.mos.controller.dto.request.OrderPaymentRequestDTO;
import com.sense.mos.infrastructure.exceptions.OrderNoFundException;
import com.sense.mos.infrastructure.exceptions.OrderPaymentFailedException;
import com.sense.mos.infrastructure.exceptions.PaymentServiceUnavailableException;
import com.sense.mos.infrastructure.feigns.PaymentClient;
import com.sense.mos.infrastructure.feigns.dto.PaymentResponseDTO;
import com.sense.mos.infrastructure.producer.MessageProducer;
import com.sense.mos.infrastructure.producer.message.OrderCancelMessage;
import com.sense.mos.infrastructure.repository.OrderRepository;
import com.sense.mos.mapper.OrderMapper;
import com.sense.mos.mapper.PaymentMapper;
import com.sense.mos.service.model.OrderModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sense.mos.common.ErrorCode.LACK_OF_BALANCE;
import static com.sense.mos.common.ErrorCode.ORDER_NOT_EXIT;
import static com.sense.mos.common.ErrorCode.PAYMENT_SYSTEM_UNAVAILABLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MessageProducer messageProducer;
    private final PaymentClient paymentClient;

    public Optional<OrderModel> findOne(Long tid) {
        return orderRepository.findById(tid)
                .map(OrderMapper.INSTANCE::toModel);
    }

    private OrderModel getOrderModelWithVerify(Long id) {
        return this.findOne(id)
                .orElseThrow(() -> new OrderNoFundException(ORDER_NOT_EXIT, "订单不存在"));
    }

    @Transactional
    public void payment(Long id, OrderPaymentRequestDTO request) {
        OrderModel model = getOrderModelWithVerify(id);
        model.payment();
        PaymentResponseDTO pay = Optional.ofNullable(
                        paymentClient.pay(PaymentMapper.INSTANCE.toRequest(request, model.getAmount())))
                .orElseThrow(() ->
                        new PaymentServiceUnavailableException(PAYMENT_SYSTEM_UNAVAILABLE, "支付平台错误，请稍后重试！"));
        if (pay.isSuccess()) {
            orderRepository.saveAndFlush(OrderMapper.INSTANCE.toEntity(model));
        } else if (pay.isLackOfBalance()) {
            throw new OrderPaymentFailedException(LACK_OF_BALANCE, "余额不足！");
        }
    }

    @Transactional
    public OrderModel cancel(Long id) {
        OrderModel model = getOrderModelWithVerify(id);
        model.cancel();
        orderRepository.saveAndFlush(OrderMapper.INSTANCE.toEntity(model));
        OrderCancelMessage orderCancellationMessage = OrderMapper.INSTANCE.toCancelMessage(model);
        messageProducer.send(orderCancellationMessage);
        return model;
    }
}
