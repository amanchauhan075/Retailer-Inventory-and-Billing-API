package com.retialerApi.repository;

import org.springframework.data.repository.CrudRepository;
import com.retialerApi.entity.Retailer;

public interface RetailerRepository extends CrudRepository<Retailer, Long> {

    // ✅ Correct method declaration
    Retailer findByUsername(String username);
}
