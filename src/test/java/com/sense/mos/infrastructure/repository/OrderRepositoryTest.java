package com.sense.mos.infrastructure.repository;

import com.sense.mos.base.IntegrationTest;
import com.sense.mos.builder.entity.OrderBuilder;
import com.sense.mos.infrastructure.repository.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.sense.mos.service.model.OrderStatus.SUBMITTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderRepositoryTest extends IntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void should_get_order_info_when_find_by_id_given_a_cid() {
        //Given
        Order persist = OrderBuilder.withDefault().persist();

        //When
        Order order = orderRepository.findById(persist.getId()).get();

        //Then
        assertEquals(persist, order);
    }

    @Test
    void should_payment_successfully_when_update_given_an_order() {

        //Given
        Order build = OrderBuilder.withDefault().withId(UUID.randomUUID().getLeastSignificantBits()).withStatus(
                SUBMITTED).build();

        //When:
        Order order = orderRepository.saveAndFlush(build);

        //Then:
        assertNotNull(order);
        assertEquals(SUBMITTED, order.getStatus());

    }
}
