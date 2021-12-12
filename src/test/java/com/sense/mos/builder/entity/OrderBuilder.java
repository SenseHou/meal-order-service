package com.sense.mos.builder.entity;

import com.sense.mos.infrastructure.repository.OrderRepository;
import com.sense.mos.infrastructure.repository.entity.Item;
import com.sense.mos.infrastructure.repository.entity.Order;
import com.sense.mos.service.model.OrderStatus;
import com.sense.mos.utils.SpringApplicationContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderBuilder {
    private final Order order = new Order();

    public static OrderBuilder withDefault() {
        return new OrderBuilder()
                .withId(1L)
                .withAmount(BigDecimal.valueOf(34.5))
                .withSubmittedAt(LocalDateTime.of(2021, 12, 12, 0, 0))
                .withReceivedAt(LocalDateTime.of(2021, 12, 12, 0, 5))
                .withStatus(OrderStatus.INITIATED)
                .withItems(Collections.singletonList(ItemBuilder.withDefault().build()));
    }

    public Order build() {
        return order;
    }

    public Order persist() {
        OrderRepository repository = SpringApplicationContext.getBean(OrderRepository.class);
        return repository.saveAndFlush(order);
    }

    public OrderBuilder withId(Long id) {
        order.setId(id);
        return this;
    }

    public OrderBuilder withAmount(BigDecimal amount) {
        order.setAmount(amount);
        return this;
    }

    public OrderBuilder withItems(List<Item> orderItems) {
        order.setItems(orderItems);
        orderItems.forEach(item -> item.setOrderId(order.getId()));
        return this;
    }

    public OrderBuilder withStatus(OrderStatus status) {
        order.setStatus(status);
        return this;
    }

    public OrderBuilder withSubmittedAt(LocalDateTime submittedAt) {
        order.setSubmittedAt(submittedAt);
        return this;
    }

    public OrderBuilder withReceivedAt(LocalDateTime receivedAt) {
        order.setReceivedAt(receivedAt);
        return this;
    }
}
