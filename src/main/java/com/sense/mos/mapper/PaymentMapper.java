package com.sense.mos.mapper;

import com.sense.mos.controller.dto.request.OrderPaymentRequestDTO;
import com.sense.mos.infrastructure.feigns.dto.PaymentRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentRequestDTO toRequest(OrderPaymentRequestDTO paymentRequest, BigDecimal amount);

}
