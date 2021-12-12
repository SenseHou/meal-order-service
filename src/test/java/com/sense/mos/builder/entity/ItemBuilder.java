package com.sense.mos.builder.entity;

import com.sense.mos.infrastructure.repository.entity.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemBuilder {
    private final Item order = new Item();

    public static ItemBuilder withDefault() {
        return new ItemBuilder()
                .withId(1L)
                .withOrderId(UUID.randomUUID().getLeastSignificantBits())
                .withQuantity(2)
                .withPrice(BigDecimal.valueOf(34.5))
                .withFinishTimeByMinute(15);
    }

    public Item build() {
        return order;
    }

    public ItemBuilder withId(Long id) {
        order.setId(id);
        return this;
    }

    public ItemBuilder withOrderId(Long orderId) {
        order.setOrderId(orderId);
        return this;
    }

    public ItemBuilder withQuantity(Integer quantity) {
        order.setQuantity(quantity);
        return this;
    }

    public ItemBuilder withPrice(BigDecimal price) {
        order.setPrice(price);
        return this;
    }

    public ItemBuilder withFinishTimeByMinute(Integer finishTimeByMinute) {
        order.setFinishTimeByMinute(finishTimeByMinute);
        return this;
    }
}
