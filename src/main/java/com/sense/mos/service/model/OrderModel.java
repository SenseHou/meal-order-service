package com.sense.mos.service.model;

import com.sense.mos.infrastructure.exceptions.OrderPaymentFailedException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.sense.mos.common.ErrorCode.ALREADY_SHIPPING;
import static com.sense.mos.common.ErrorCode.MEAL_PREPARING;
import static com.sense.mos.service.model.OrderStatus.CANCELED;
import static com.sense.mos.service.model.OrderStatus.PREPARING;
import static com.sense.mos.service.model.OrderStatus.SHIPPING;
import static com.sense.mos.service.model.OrderStatus.SUBMITTED;
import static java.time.LocalDateTime.now;

@Getter
@Setter
public class OrderModel {
    private Long id;
    private LocalDateTime submittedAt;
    private LocalDateTime receivedAt;
    private OrderStatus status;
    private BigDecimal amount;
    List<ItemModel> items;

    public void payment() {
        status = SUBMITTED;
        submittedAt = now();
    }

    public void received() {
        status = PREPARING;
        receivedAt = now();
    }

    public void cancel() {
        if (isMealsNotOvertime()) {
            throw new OrderPaymentFailedException(MEAL_PREPARING, "备餐中，无法取消！");
        }
        if (SHIPPING.equals(status)) {
            throw new OrderPaymentFailedException(ALREADY_SHIPPING, "配送中，无法取消！");
        }
        status = CANCELED;
    }

    private boolean isMealsNotOvertime() {
        return PREPARING.equals(status) && Duration.between(now(), receivedAt).toMinutes()
                < maxFinishedTime();
    }

    private Integer maxFinishedTime() {
        return items.stream().map(ItemModel::getFinishTimeByMinute).max(Integer::compare).orElse(0);
    }
}
