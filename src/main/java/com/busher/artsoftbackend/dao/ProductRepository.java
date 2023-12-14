package com.busher.artsoftbackend.dao;

import com.busher.artsoftbackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
