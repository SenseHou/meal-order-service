package com.sense.mos.builder.dto;

import com.sense.mos.controller.dto.response.OrderCancelResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCancelResponseDTOBuilder {
    private final OrderCancelResponseDTO dto = new OrderCancelResponseDTO();

    public static OrderCancelResponseDTOBuilder withDefault() {
        return new OrderCancelResponseDTOBuilder()
                .withCode("SUCCESS")
                .withMessage("取消成功");
    }

    public OrderCancelResponseDTO build() {
        return dto;
    }

    public OrderCancelResponseDTOBuilder withCode(String code) {
        dto.setCode(code);
        return this;
    }

    public OrderCancelResponseDTOBuilder withMessage(String message) {
        dto.setMessage(message);
        return this;
    }
}
