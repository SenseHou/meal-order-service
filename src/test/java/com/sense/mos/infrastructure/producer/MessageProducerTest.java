package com.sense.mos.infrastructure.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sense.mos.builder.OrderCancelMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.data.mapping.Alias.ofNullable;

public class MessageProducerTest {

    private MessageProducer messageProducer;
    private RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    protected void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        messageProducer = new MessageProducer(rabbitTemplate, objectMapper);
    }

    @Test
    void should_return_true_when_send_message_given_message() {

        // When:
        boolean sent = messageProducer.send(OrderCancelMessageBuilder.withDefault().build());

        // Then:
        assertTrue(sent);
    }

    @Test
    void should_return_false_when_send_message_given_rabbit_template_throw_exception() {

        //Given:
        doThrow(AmqpException.class).when(rabbitTemplate).convertAndSend(anyString(), ofNullable(any()));

        //When:
        boolean sent = messageProducer.send(OrderCancelMessageBuilder.withDefault().build());

        //Then:
        assertFalse(sent);
    }

}
