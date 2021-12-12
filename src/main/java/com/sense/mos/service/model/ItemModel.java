package com.sense.mos.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemModel {
    private Integer finishTimeByMinute;
    private BigDecimal price;
    private Integer quantity;
    private Long id;
    private Long orderId;
}
