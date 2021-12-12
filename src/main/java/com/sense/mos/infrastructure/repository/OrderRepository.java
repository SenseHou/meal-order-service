package com.sense.mos.infrastructure.repository;

import com.sense.mos.infrastructure.repository.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
