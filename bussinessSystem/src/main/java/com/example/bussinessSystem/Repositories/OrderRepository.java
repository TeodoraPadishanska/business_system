package com.example.bussinessSystem.Repositories;

import com.example.bussinessSystem.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
