package com.sense.mos.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "meal-order-queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

}
