package com.sense.mos.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderCancelResponseDTO {
    private String code;
    private String message;

    public static OrderCancelResponseDTO success() {
        return new OrderCancelResponseDTO("SUCCESS", "取消成功");
    }
}
