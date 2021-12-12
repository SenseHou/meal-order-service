package com.sense.mos.infrastructure.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducer {

    public static final String ROUTING_KEY = "meal-order-routing-key";

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public boolean send(Object messageObject) {
        try {
            String message = objectMapper.writeValueAsString(messageObject);
            rabbitTemplate.convertAndSend(ROUTING_KEY, message);
            return true;
        } catch (Exception e) {
            log.error("send message {} error {}", ROUTING_KEY, messageObject, e);
            return false;
        }
    }
}
