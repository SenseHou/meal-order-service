package com.sense.mos.mapper;

import com.sense.mos.infrastructure.producer.message.OrderCancelMessage;
import com.sense.mos.infrastructure.repository.entity.Order;
import com.sense.mos.service.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderModel toModel(Order entity);

    Order toEntity(OrderModel model);

    OrderCancelMessage toCancelMessage(OrderModel model);

}
