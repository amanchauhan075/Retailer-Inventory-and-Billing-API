package com.retialerApi.repository;


import com.retialerApi.entity.orders;


import java.time.LocalDateTime;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<orders, Long> {

	
	List<orders> findByRetailerUsernameAndOrderDateBetween(String retailerUsername, LocalDateTime start, LocalDateTime end);



	List<orders> findByRetailerUsername(String retailerUsername);

	
}


