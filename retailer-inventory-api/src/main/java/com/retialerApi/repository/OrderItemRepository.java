package com.retialerApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.retialerApi.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi")
	double findTotalRevenue();
}