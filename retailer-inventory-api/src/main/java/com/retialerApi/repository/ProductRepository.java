package com.retialerApi.repository;

import com.retialerApi.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByRetailerUsername(String username);
    List<Product> findByNameContainingIgnoreCaseAndRetailerUsername(String keyword, String username);
    List<Product> findByStockLessThan(int threshold);
    List<Product> findByStockLessThanAndRetailerUsername(int threshold, String username);
}
